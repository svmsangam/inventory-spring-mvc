package com.inventory.core.api.impl;

import com.inventory.core.api.iapi.IStoreInfoApi;
import com.inventory.core.model.converter.StoreInfoConverter;
import com.inventory.core.model.dto.StoreInfoDTO;
import com.inventory.core.model.entity.StoreInfo;
import com.inventory.core.model.enumconstant.Status;
import com.inventory.core.model.repository.StoreInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreInfoApi implements IStoreInfoApi{

    @Autowired
    private StoreInfoConverter storeInfoConverter;

    @Autowired
    private StoreInfoRepository storeInfoRepository;

    @Override
    public StoreInfoDTO save(StoreInfoDTO storeInfoDTO) {

        StoreInfo storeInfo = storeInfoConverter.convertToEntity(storeInfoDTO);
        storeInfo.setStatus(Status.ACTIVE);

        return storeInfoConverter.convertToDto(storeInfoRepository.save(storeInfo));
    }

    @Override
    public StoreInfoDTO update(StoreInfoDTO storeInfoDTO) {

        StoreInfo storeInfo = storeInfoRepository.findById(storeInfoDTO.getStoreId());

        storeInfo = storeInfoConverter.copyConvertToEntity(storeInfoDTO, storeInfo);

        return storeInfoConverter.convertToDto(storeInfoRepository.save(storeInfo));
    }

    @Override
    public void delete(long storeId) {

        StoreInfo storeInfo = storeInfoRepository.findById(storeId);
        storeInfo.setStatus(Status.DELETED);

        storeInfoRepository.save(storeInfo);
    }

    @Override
    public StoreInfoDTO show(long storeId, Status status) {
        return storeInfoConverter.convertToDto(storeInfoRepository.findByIdAndStatus(storeId,status));
    }

    @Override
    public List<StoreInfoDTO> list(Status status) {
        return storeInfoConverter.convertToDtoList(storeInfoRepository.findAllByStatus(status));
    }

    @Override
    public StoreInfoDTO getStoreByNameAndStatus(String storeName, long storeId) {
        return null;
    }

    @Override
    public long storeCount(Status status, long storeId) {
        return 0;
    }
}
