package com.inventory.core.model.converter;

import com.inventory.core.model.dto.ClientInfoDTO;
import com.inventory.core.model.entity.AccountInfo;
import com.inventory.core.model.entity.ClientInfo;
import com.inventory.core.model.enumconstant.AccountAssociateType;
import com.inventory.core.model.enumconstant.Status;
import com.inventory.core.model.repository.AccountInfoRepository;
import com.inventory.core.model.repository.CityInfoRepository;
import com.inventory.core.model.repository.StoreInfoRepository;
import com.inventory.core.model.repository.UserRepository;
import com.inventory.core.util.IConvertable;
import com.inventory.core.util.IListConvertable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClientInfoConverter implements IListConvertable<ClientInfo , ClientInfoDTO> , IConvertable<ClientInfo , ClientInfoDTO>{
    
    @Autowired
    private CityInfoRepository cityInfoRepository;
    
    @Autowired
    private CityInfoConverter cityInfoConverter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Autowired
    private StoreInfoRepository storeInfoRepository;
    
    @Override
    public ClientInfo convertToEntity(ClientInfoDTO dto) {
        return copyConvertToEntity(dto , new ClientInfo());
    }

    @Override
    public ClientInfoDTO convertToDto(ClientInfo entity) {
        
        if (entity == null) {
            return null;
        }

        ClientInfoDTO dto = new ClientInfoDTO();
        
        dto.setClientId(entity.getId());

        if (entity.getCityInfo() != null) {
            dto.setCityId(entity.getCityInfo().getId());
        }

        if (entity.getStoreInfo() != null){
            dto.setStoreInfoId(entity.getStoreInfo().getId());
        }

        dto.setCityInfoDTO(cityInfoConverter.convertToDto(entity.getCityInfo()));
        dto.setClientType(entity.getClientType());
        dto.setCompanyName(entity.getCompanyName());
        dto.setContact(entity.getContact());
        dto.setCreatedById(entity.getCreatedBy().getId());
        dto.setCreatedByName(entity.getCreatedBy().getUsername());
        dto.setEmail(entity.getEmail());
        dto.setMobileNumber(entity.getMobileNumber());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setStreet(entity.getStreet());
        dto.setVersion(entity.getVersion());

        AccountInfo accountInfo = accountInfoRepository.findByAssociateIdAndAssociateType(entity.getId() , AccountAssociateType.CUSTOMER);

        if (accountInfo != null){
            dto.setAccountId(accountInfo.getId());
            dto.setAccountNo(accountInfo.getAcountNumber());
        }
        
        return dto;
    }

    @Override
    public ClientInfo copyConvertToEntity(ClientInfoDTO dto, ClientInfo entity) {
        
        if (entity == null | dto == null){
            
            return null;
        }

        if (dto.getCityId() != null) {
            entity.setCityInfo(cityInfoRepository.findByIdAndStatus(dto.getCityId(), Status.ACTIVE));
        }

        entity.setClientType(dto.getClientType());
        entity.setCompanyName(dto.getCompanyName());
        entity.setContact(dto.getContact());
        entity.setCreatedBy(userRepository.findById(dto.getCreatedById()));
        entity.setEmail(dto.getEmail());
        entity.setMobileNumber(dto.getMobileNumber());
        entity.setName(dto.getName());
        entity.setStreet(dto.getStreet());
        entity.setStoreInfo(storeInfoRepository.findById(dto.getStoreInfoId()));
        
        return entity;
    }

    @Override
    public List<ClientInfoDTO> convertToDtoList(List<ClientInfo> entities) {
        return entities.parallelStream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<ClientInfo> convertToEntityList(List<ClientInfoDTO> dtoList) {
        return dtoList.parallelStream().filter(Objects::nonNull).map(this::convertToEntity).collect(Collectors.toList());
    }
}
