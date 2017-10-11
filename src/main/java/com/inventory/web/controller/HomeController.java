package com.inventory.web.controller;

import com.inventory.core.api.iapi.IInvoiceInfoApi;
import com.inventory.core.api.iapi.IOrderInfoApi;
import com.inventory.core.api.iapi.IStockInfoApi;
import com.inventory.core.api.iapi.IUserApi;
import com.inventory.core.model.dto.InvUserDTO;
import com.inventory.core.model.enumconstant.Status;
import com.inventory.core.util.Authorities;
import com.inventory.web.util.AuthenticationUtil;
import com.inventory.web.util.ParameterConstants;
import com.inventory.web.util.StringConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class HomeController {

    @Autowired
    private IUserApi userApi;

    @Autowired
    private IStockInfoApi stockInfoApi;

    @Autowired
    private IInvoiceInfoApi invoiceInfoApi;

    @Autowired
    private IOrderInfoApi orderInfoApi;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String toTestJspPage() {

        return "invoice/show";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage(HttpServletRequest request, @ModelAttribute("message") String message, RedirectAttributes redirectAttributes) throws IOException {

        if (AuthenticationUtil.getCurrentUser(userApi) == null) {
            //redirectAttributes.addFlashAttribute(StringConstants.ERROR , "Athentication failed");
            return "dashboard/login";
        }

        redirectAttributes.addFlashAttribute(ParameterConstants.PARAM_MESSAGE, message);
        return "redirect:/dashboard";

    }


    //for business owner

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String getDashboard(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes , ModelMap modelMap) throws IOException {

        InvUserDTO currentUser = AuthenticationUtil.getCurrentUser(userApi);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute(StringConstants.ERROR, "Athentication failed");
            return "redirect:/logout";
        }

        if (currentUser.getStoreId() == null) {
            redirectAttributes.addFlashAttribute(StringConstants.ERROR, "Store not assigned");
            return "redirect:/logout";//store not assigned page
        }

        if (currentUser.getUserauthority().contains(Authorities.SUPERADMIN) && currentUser.getUserauthority().contains(Authorities.AUTHENTICATED)) {

            modelMap.put(StringConstants.TOTALSTOCK , stockInfoApi.getTotalStockByStoreInfoAndStatus(currentUser.getStoreId() , Status.ACTIVE));
            modelMap.put(StringConstants.TOTALSALE , invoiceInfoApi.getTotalAmountByStoreInfoAndStatus(currentUser.getStoreId() , Status.ACTIVE));
            modelMap.put(StringConstants.TOTALUSER , userApi.getTotalUserByStoreInfoAndStatus(currentUser.getStoreId() , Status.ACTIVE));
            modelMap.put(StringConstants.ORDER_LIST , orderInfoApi.listSale(Status.ACTIVE , currentUser.getStoreId() , 0 , 6));
        }


        return "dashboard/index";


    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String getLogin(@RequestParam(value = "error", required = false) Boolean error, HttpServletRequest request, ModelMap modelMap) throws IOException {

        if (error == null){
            error = false;
        }

        if (error) {
            modelMap.put(StringConstants.ERROR, "wrong username or password");
        }
        return "dashboard/login";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String error(HttpServletRequest request) {

        return "static/404";
    }

    @RequestMapping(value = "/400", method = RequestMethod.GET)
    public String dataNotFound(HttpServletRequest request) {

        return "static/400";
    }

    @RequestMapping(value = "/401", method = RequestMethod.GET)
    public String accessDeniled(HttpServletRequest request) {

        return "static/401";
    }

    @RequestMapping(value = "/500", method = RequestMethod.GET)
    public String errorpage(HttpServletRequest request) {

        return "static/500";
    }

}
