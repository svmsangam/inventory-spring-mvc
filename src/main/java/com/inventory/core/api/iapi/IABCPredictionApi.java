package com.inventory.core.api.iapi;

import com.inventory.core.model.dto.RestResponseDTO;

public interface IABCPredictionApi {
    RestResponseDTO getPredictedData(Integer id, String productName, String category);
}
