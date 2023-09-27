package com.inventory.core.api.impl;

import com.inventory.core.api.iapi.IABCPredictionApi;
import com.inventory.core.model.dto.RestResponseDTO;
import com.inventory.web.util.DateUtil;
import com.inventory.web.util.StringConstants;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@Service
public class ABCPredictionApi implements IABCPredictionApi {

    @Override
    public RestResponseDTO getPredictedData(Integer id, String productName, String category) {
        RestResponseDTO responseDTO = new RestResponseDTO();
        String requestPayloadJson;
        String apiUrl = StringConstants.PREDICTION_URL;
        Map<String, Integer> resultMap = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("productName", productName);
        requestPayload.put("category", category);
        requestPayload.put("month", DateUtil.getMonthInitials());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            requestPayloadJson = objectMapper.writeValueAsString(requestPayload);
        } catch (Exception e) {
            responseDTO.setStatus("Failure");
            return responseDTO;
        }

            HttpEntity<String> requestEntity = new HttpEntity<>(requestPayloadJson, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                try {
                    resultMap = objectMapper.readValue(responseEntity.getBody(), Map.class);
                    responseDTO.setDetail(resultMap);
                    responseDTO.setMessage("Predicted stock for"+" "+productName+" "+"for the month of "+DateUtil.getMonthFull());
                    return responseDTO;
                }catch (Exception e){
                    responseDTO.setStatus("Failure");
                    return responseDTO;
                }
            } else {
                responseDTO.setStatus("Failure");
                return responseDTO;
            }
    }
}
