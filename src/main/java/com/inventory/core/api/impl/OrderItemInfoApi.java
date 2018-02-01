package com.inventory.core.api.impl;

import com.inventory.core.api.iapi.IOrderItemInfoApi;
import com.inventory.core.model.converter.OrderItemInfoConverter;
import com.inventory.core.model.dto.OrderInfoDTO;
import com.inventory.core.model.dto.OrderItemInfoDTO;
import com.inventory.core.model.entity.OrderItemInfo;
import com.inventory.core.model.enumconstant.Status;
import com.inventory.core.model.repository.OrderItemInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhiraj on 9/10/17.
 */
@Service
public class OrderItemInfoApi implements IOrderItemInfoApi {

    @Autowired
    private OrderItemInfoRepository orderItemInfoRepository;

    @Autowired
    private OrderItemInfoConverter orderItemInfoConverter;

    @Override
    public double save(OrderInfoDTO orderInfoDTO) {

        double amount = 0.0;

        List<OrderItemInfo> orderItemInfoList = new ArrayList<>();

        for (OrderItemInfoDTO dto : orderInfoDTO.getOrderItemInfoDTOList()){

            dto.setOrderInfoId(orderInfoDTO.getOrderId());

            OrderItemInfo orderItemInfo = orderItemInfoConverter.convertToEntity(dto);

            orderItemInfo.setStatus(Status.ACTIVE);

            amount = amount + orderItemInfo.getAmount();

            orderItemInfoList.add(orderItemInfo);
        }

        orderItemInfoRepository.save(orderItemInfoList);

        return amount;
    }

    @Override
    public List<OrderItemInfoDTO> getAllByStatusAndOrderInfo(Status status, long orderId) {
        return orderItemInfoConverter.convertToDtoList(orderItemInfoRepository.findAllByStatusAndOrderInfo(status , orderId));
    }

    @Override
    public double getTotalAmountByStatusAndOrderInfo(Status status, long orderId) {

        Double amount = orderItemInfoRepository.findTotalAmountByStatusAndOrderInfo(status , orderId);

        if (amount == null){
            return 0;
        }

        return amount;
    }

    @Override
    public double getTotalSaleAmountOfItem(long itemId) {

        Double amount = orderItemInfoRepository.findTotalSaleAmountOfItem(itemId);

        if (amount == null){
            return 0;
        }

        return amount;
    }

    @Override
    public double getTotalSaleAmountOfProduct(long productId) {

        Double amount = orderItemInfoRepository.findTotalSaleAmountOfProduct(productId);

        if (amount == null){
            return 0;
        }

        return amount;
    }
}
