package com.inventory.web.session;

import com.inventory.core.api.iapi.ISessionApi;
import com.inventory.core.model.entity.UserSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.ArrayList;
import java.util.List;

public class PersistingSessionRegistry implements SessionRegistry, ApplicationListener<SessionDestroyedEvent> {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private ISessionApi sessionApi;

	public PersistingSessionRegistry(ISessionApi sessionApi) {
		this.sessionApi = sessionApi;
	}

	public void onApplicationEvent(SessionDestroyedEvent event) {
		String sessionId = event.getId();
		removeSessionInformation(sessionId);
	}

	@Override
	public List<Object> getAllPrincipals() {
		// WARN: should not use this
		logger.warn("Should not call this method");
		return new ArrayList<Object>();
	}

	@Override
	public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
		logger.debug("includeExpiredSessions==>" + includeExpiredSessions);
		logger.debug("principal==>" + principal);
		if (principal instanceof UserDetailsWrapper) {
			List<UserSession> sessions = sessionApi
					.getAllUserSession(((UserDetailsWrapper) principal).getUser().getId(), includeExpiredSessions);
			logger.debug("sessions==>" + sessions);
			return convert(sessions);
		}
		return new ArrayList<SessionInformation>();
	}

	@Override
	public SessionInformation getSessionInformation(String sessionId) {
		UserSession userSession = sessionApi.getUserSession(sessionId);
		if (userSession != null) {
			return convert(userSession);
		}
		return null;
	}

	@Override
	public void refreshLastRequest(String sessionId) {
		sessionApi.refreshSession(sessionId);

	}

	@Override
	public void registerNewSession(String sessionId, Object principal) {
		if (principal instanceof UserDetailsWrapper) {
			logger.debug("register session for: " + ((UserDetailsWrapper) principal).getUsername());
			sessionApi.registerNewSession(sessionId, (UserDetailsWrapper) principal);
		}

	}

	@Override
	public void removeSessionInformation(String sessionId) {
		sessionApi.removeSession(sessionId);

	}

	private SessionInformation convert(UserSession session) {
		logger.debug("session.getUser()==>" + session.getUser());
		logger.debug("session.getUser().getAuthority()==>" + session.getUser().getAuthority());
		UserDetailsWrapper wrapper = new UserDetailsWrapper(session.getUser(),
				AuthorityUtils.commaSeparatedStringToAuthorityList(session.getUser().getAuthority()));
		SessionInformation si = new SessionInformation(wrapper, session.getSessionId(), session.getLastRequest());
		logger.debug("si==>" + si);
		if (session.isExpired()) {
			si.expireNow();
		}
		return si;
	}

	private List<SessionInformation> convert(List<UserSession> sessions) {
		List<SessionInformation> infos = new ArrayList<SessionInformation>();
		for (UserSession s : sessions) {
			infos.add(convert(s));
		}
		return infos;
	}

}
