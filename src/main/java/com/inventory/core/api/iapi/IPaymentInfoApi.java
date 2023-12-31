package com.inventory.core.api.iapi;

import com.inventory.core.model.dto.PaymentInfoDTO;
import com.inventory.core.model.enumconstant.Status;

import java.util.List;

/**
 * Created by dhiraj on 10/10/17.
 */
public interface IPaymentInfoApi {
    
    PaymentInfoDTO save(PaymentInfoDTO paymentInfoDTO);

    void refundOnInvoiceCancel(long invoiceId, long createdById);

    void refundOnSalesReturn(long invoiceId, long createdById, double amount);

    long collectChuque(long paymentInfoId);

    PaymentInfoDTO getById(long paymentInfoId);

    PaymentInfoDTO getByIdAndStatus(long paymentInfoId , Status status);

    PaymentInfoDTO show(long paymentInfoId , Status status , long storeId);

    PaymentInfoDTO getByIdAndStatusAndStoreAndInvoiceInfo(long paymentInfoId , Status status , long storeId , long invoiceInfoId);

    List<PaymentInfoDTO> getAllByStatusInAndStoreAndInvoiceInfo(List<Status> status , long storeId , long invoiceInfoId);

    double getTotalPaymentByStoreInfoAndStatus(long storeInfoId , Status status);

    double getToDayTotalPaymentByStoreInfoAndStatus(long storeInfoId , Status status);

}
