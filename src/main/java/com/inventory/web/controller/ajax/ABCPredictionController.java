package com.inventory.web.controller.ajax;


import com.inventory.core.api.iapi.IABCPredictionApi;
import com.inventory.core.model.dto.RestResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/prediction")
public class ABCPredictionController {

    @Autowired
    private IABCPredictionApi predictionApi;

    @PostMapping(value = "/result")
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMINISTRATOR','ROLE_ADMINISTRATOR','ROLE_USER,ROLE_AUTHENTICATED')")
    public ResponseEntity<RestResponseDTO> getPredictionResult(
            @RequestParam(value = "productId", required = true) Integer id,
            @RequestParam(value = "productName", required = true) String productName,
            @RequestParam(value = "category", required = true) String category)
    {
        return new ResponseEntity<RestResponseDTO>(predictionApi.getPredictedData(id,productName,category), HttpStatus.OK);
    }
}
