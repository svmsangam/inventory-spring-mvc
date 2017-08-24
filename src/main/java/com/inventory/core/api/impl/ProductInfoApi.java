package com.inventory.core.api.impl;

import com.inventory.core.api.iapi.IProductInfoApi;
import com.inventory.core.api.iapi.IStockInfoApi;
import com.inventory.core.model.converter.ProductInfoConverter;
import com.inventory.core.model.dto.ProductInfoDTO;
import com.inventory.core.model.entity.ProductInfo;
import com.inventory.core.model.enumconstant.Status;
import com.inventory.core.model.repository.ProductInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhiraj on 8/22/17.
 */
@Transactional
@Service
public class ProductInfoApi implements IProductInfoApi{

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductInfoConverter productInfoConverter;

    @Autowired
    private IStockInfoApi stockInfoApi;

    @Override
    public ProductInfoDTO save(ProductInfoDTO productInfoDTO) {

        ProductInfo productInfo = productInfoConverter.convertToEntity(productInfoDTO);

        productInfo.setStatus(Status.ACTIVE);

        productInfo = productInfoRepository.save(productInfo);

        stockInfoApi.saveOnProductSave(productInfo.getId());

        return productInfoConverter.convertToDto(productInfo);
    }

    @Override
    public ProductInfoDTO update(ProductInfoDTO productInfoDTO) {

        ProductInfo productInfo = productInfoRepository.findById(productInfoDTO.getProductId());

        productInfo = productInfoConverter.copyConvertToEntity(productInfoDTO , productInfo);

        return productInfoConverter.convertToDto(productInfoRepository.save(productInfo));
    }

    @Override
    public void delete(long productInfoId) {

        ProductInfo productInfo = productInfoRepository.findById(productInfoId);

        productInfo.setStatus(Status.DELETED);

        productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfoDTO getByNameAndStoreAndStatus(String productName, long storeId, Status status) {
        return null;
    }

    @Override
    public ProductInfoDTO getByIdAndStoreAndStatus(long productInfoId, long storeId, Status status) {
        return productInfoConverter.convertToDto(productInfoRepository.findByIdAndStatusAndStoreInfo(productInfoId , status , storeId));
    }

    @Override
    public List<ProductInfoDTO> list(Status status, long storeId) {
        return productInfoConverter.convertToDtoList(productInfoRepository.findAllByStatusAndStoreInfo(status , storeId));
    }
}
