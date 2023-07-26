package com.inventory.web.controller;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inventory.core.api.iapi.ICityInfoApi;
import com.inventory.core.api.iapi.ISendMailSSL;
import com.inventory.core.api.iapi.IServiceInfoApi;
import com.inventory.core.api.iapi.IStoreUserInfoApi;
import com.inventory.core.api.iapi.ISubscriberApi;
import com.inventory.core.api.iapi.ISubscriberServiceApi;
import com.inventory.core.api.iapi.IUserApi;
import com.inventory.core.api.impl.RecaptchaService;
import com.inventory.core.model.dto.InvUserDTO;
import com.inventory.core.model.dto.SubscriberDTO;
import com.inventory.core.model.dto.SubscriberServiceDTO;
import com.inventory.core.model.enumconstant.Status;
import com.inventory.core.validation.SubscriberValidation;
import com.inventory.web.error.RenewError;
import com.inventory.web.error.SubscriberError;
import com.inventory.web.util.AuthenticationUtil;
import com.inventory.web.util.LoggerUtil;
import com.inventory.web.util.RequestUtils;
import com.inventory.web.util.StringConstants;

/**
 * Created by dhiraj on 1/25/18.
 */
@Controller
@RequestMapping("subscriber")
public class SubscriberController {

	@Autowired
	private RecaptchaService captchaService;

	@Autowired
	private IUserApi userApi;

	@Autowired
	private ISubscriberApi subscriberApi;

	@Autowired
	private IServiceInfoApi serviceInfoApi;

	@Autowired
	private ICityInfoApi cityInfoApi;

	@Autowired
	private IStoreUserInfoApi storeUserInfoApi;

	@Autowired
	private SubscriberValidation subscriberValidation;

	@Autowired
	private ISendMailSSL mailApi;

	@Autowired
	private ISubscriberServiceApi subscriberServiceApi;

	@GetMapping(value = "/list")
	@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public String list(ModelMap modelMap, RedirectAttributes redirectAttributes) {

		try {

			modelMap.put(StringConstants.SUBSCRIBER_LIST, subscriberApi.list(Status.ACTIVE));

			/* current user checking end */

		} catch (Exception e) {

			LoggerUtil.logException(this.getClass(), e);
			return "redirect:/500";
		}

		return "subscriber/list";
	}

	@GetMapping(value = "/add")
	@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public String add(ModelMap modelMap, RedirectAttributes redirectAttributes) {

		try {

			/* current user checking start */
			InvUserDTO currentUser = AuthenticationUtil.getCurrentUser(userApi);

			/* current user checking end */

			modelMap.put(StringConstants.SERVICE_LIST, serviceInfoApi.list(Status.ACTIVE));
			modelMap.put(StringConstants.CITY_LIST, cityInfoApi.list());

		} catch (Exception e) {

			LoggerUtil.logException(this.getClass(), e);
			return "redirect:/500";
		}

		return "subscriber/add";
	}

	@PostMapping(value = "/save")
	@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public String save(@ModelAttribute("subscriber") SubscriberDTO subscriberDTO, ModelMap modelMap,
			RedirectAttributes redirectAttributes) {

		try {

			/* current user checking start */
			InvUserDTO currentUser = AuthenticationUtil.getCurrentUser(userApi);
			String password = subscriberDTO.getPassword();
			/* current user checking end */

			subscriberDTO.setCreatedById(currentUser.getUserId());

			subscriberDTO = subscriberApi.save(subscriberDTO);

			mailApi.sendHtmlMail(StringConstants.VerificationMainSender, subscriberDTO.getEmail(),
					getAccountCreatedMsg(subscriberDTO.getUsername(),password),"subscriber created");

		} catch (Exception e) {
			LoggerUtil.logException(this.getClass(), e);
			return "redirect:/500";
		}

		return "redirect:/subscriber/show?subscriberId=" + subscriberDTO.getSubscriberId();
	}

	@GetMapping(value = "/show")
	@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public String show(@RequestParam("subscriberId") long subscriberId, ModelMap modelMap,
			RedirectAttributes redirectAttributes) {

		try {

			/* current user checking start */
			InvUserDTO currentUser = AuthenticationUtil.getCurrentUser(userApi);

			/* current user checking end */

			SubscriberDTO subscriberDTO = subscriberApi.show(Status.ACTIVE, subscriberId);

			if (subscriberDTO == null) {
				redirectAttributes.addFlashAttribute(StringConstants.ERROR, "subscriber not found");

				return "redirect:/subscriber/list";
			}

			modelMap.put(StringConstants.SUBSCRIBER, subscriberDTO);
			modelMap.put(StringConstants.STORE_LIST, storeUserInfoApi.getAllStoreByUser(subscriberDTO.getUserId()));
			modelMap.put(StringConstants.USER_LIST, storeUserInfoApi.getAllUserBySuperAdmin(subscriberDTO.getUserId()));
			modelMap.put(StringConstants.SUBSCRIBER_SERVICE_LIST,
					subscriberServiceApi.list(Status.ACTIVE, subscriberId));
			modelMap.put(StringConstants.SERVICE_LIST, serviceInfoApi.list(Status.ACTIVE));

		} catch (Exception e) {
			LoggerUtil.logException(this.getClass(), e);
			return "redirect:/500";
		}

		return "subscriber/show";
	}

	@GetMapping(value = "/activate")
	@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public String activate(@RequestParam("subid") long subscriberId, RedirectAttributes redirectAttributes) {

		try {

			/* current user checking start */
			InvUserDTO currentUser = AuthenticationUtil.getCurrentUser(userApi);

			/* current user checking end */

			subscriberApi.activate(subscriberId);

		} catch (Exception e) {
			LoggerUtil.logException(this.getClass(), e);
			return "redirect:/500";
		}

		return "redirect:/subscriber/show?subscriberId=" + subscriberId;
	}

	@GetMapping(value = "/service/renew")
	@PreAuthorize("hasRole('ROLE_SYSTEM')")
	public String renew(@RequestParam("subscriberId") long subscriberId, @RequestParam("serviceId") long serviceId,
			ModelMap modelMap, RedirectAttributes redirectAttributes) {

		try {

			/* current user checking start */
			InvUserDTO currentUser = AuthenticationUtil.getCurrentUser(userApi);

			/* current user checking end */

			synchronized (this) {

				SubscriberDTO subscriberDTO = subscriberApi.show(Status.ACTIVE, subscriberId);

				if (subscriberDTO == null) {
					redirectAttributes.addFlashAttribute(StringConstants.ERROR, "subscriber not found");

					return "redirect:/subscriber/list";
				}

				RenewError error = subscriberValidation.onRenew(subscriberDTO.getUserId(), serviceId);

				if (!error.isValid()) {

					redirectAttributes.addFlashAttribute(StringConstants.ERROR, error.getError());

					return "redirect:/subscriber/show?subscriberId=" + subscriberId;
				}

				SubscriberServiceDTO subscriberServiceDTO = subscriberServiceApi.save(serviceId, subscriberId);

				mailApi.sendHtmlMail(StringConstants.VerificationMainSender, subscriberDTO.getEmail(),
						getRenewMsg(subscriberServiceDTO.getServiceInfo().getTitle(),
								subscriberServiceDTO.getExpireOn(), subscriberDTO.getFullName(),
								subscriberServiceDTO.getServiceInfo().getTotalStore()),
						"account renew");
				redirectAttributes.addFlashAttribute(StringConstants.MESSAGE, "successfully renewed");

			}

		} catch (Exception e) {
			LoggerUtil.logException(this.getClass(), e);
			return "redirect:/500";
		}

		return "redirect:/subscriber/show?subscriberId=" + subscriberId;
	}

	private String getRenewMsg(String serviceName, Date expireOn, String subscribername, int totalStore) {

		String msg = "";

		msg = "dear " + subscribername + " " + serviceName + " service is successfull renewed and will be expire on "
				+ expireOn + " now your are able to manage " + totalStore + " stores thank you";

		return msg;
	}
	private String getAccountCreatedMsg(String username, String password) {

		String msg = "";

		msg = "Dear " + username + ", your email have been registered to makalu-inventory with "
				+"username: "+username+" password: "+password;

		return msg;
	}

	@GetMapping(value = "/register")
	public String register(ModelMap modelMap, RedirectAttributes redirectAttributes) {

		try {

			modelMap.put(StringConstants.CITY_LIST, cityInfoApi.list());

		} catch (Exception e) {

			LoggerUtil.logException(this.getClass(), e);
			return "redirect:/500";
		}

		return "subscriber/register";
	}

	@PostMapping("/register")
	public String signupDemo(@ModelAttribute("subscriber") SubscriberDTO subscriberDTO,
			@RequestParam(name = "g-recaptcha-response") String recaptchaResponse, HttpServletRequest request,
			RedirectAttributes redirectAttributes, ModelMap modelMap) {

		try {

			if (recaptchaResponse == null) {
				modelMap.put(StringConstants.CITY_LIST, cityInfoApi.list());
				modelMap.put(StringConstants.SUBSCRIBER, subscriberDTO);
				modelMap.put(StringConstants.ERROR, "please verify captcha");

				return "subscriber/register";
			}

			synchronized (this) {

				SubscriberError error = subscriberValidation.onRegister(subscriberDTO);

				if (!error.isValid()) {

					modelMap.put(StringConstants.CITY_LIST, cityInfoApi.list());
					modelMap.put(StringConstants.SUBSCRIBER, subscriberDTO);
					modelMap.put(StringConstants.SUBSCRIBER_ERROR, error);

					return "subscriber/register";

				}

				String ip = request.getRemoteAddr();

				String captchaVerifyMessage = captchaService.verifyRecaptcha(ip, recaptchaResponse);

				if (StringUtils.isNotEmpty(captchaVerifyMessage)) {

					redirectAttributes.addFlashAttribute(StringConstants.ERROR, "invalid captcha ");

					return "redirect:/subscriber/register";

				}

				String token = subscriberApi.register(subscriberDTO);

				mailApi.sendHtmlMail(StringConstants.VerificationMainSender, subscriberDTO.getEmail(),
						getVerificationMsg(token, RequestUtils.getServerUlr(request)), "email verification request");

				redirectAttributes.addFlashAttribute(StringConstants.MESSAGE,
						"successfully registered please check your email to activate account");

			}
		} catch (ParseException e) {
			LoggerUtil.logException(this.getClass(), e);
			return "redirect:/500";
		}

		return "redirect:/login";
	}

	private String getVerificationMsg(String token, String url) {

		String msg = "";

		url = url + "/user/activate?token=" + token;

		msg = "to activate your account <a href='" + url
				+ "' style='border-color: #367fa9; border-radius: 3px;'>click here</a>";

		return msg;
	}

}
