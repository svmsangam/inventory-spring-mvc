package com.inventory.core.api.iapi;

import com.inventory.core.model.dto.InvoiceFilterDTO;
import com.inventory.core.model.dto.InvoiceInfoDTO;
import com.inventory.core.model.dto.InvoiceListDTO;
import com.inventory.core.model.dto.PaymentInfoDTO;
import com.inventory.core.model.enumconstant.Status;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by dhiraj on 9/13/17.
 */
public interface IInvoiceInfoApi {

    double getTotalAmountByStoreInfoAndStatus(long storeInfoId , Status status);

    double getToDayTotalAmountByStoreInfoAndStatus(long storeInfoId , Status status);

    String generatInvoiceNumber(long storeId);

    InvoiceInfoDTO save(long orderInfoId , long createdById);

    InvoiceInfoDTO saveQuickSale(PaymentInfoDTO paymentInfoDTO);

    void updateOnPayment(long paymentInfoId);

    void updateVersion(long invoiceId);

    List<InvoiceInfoDTO> filter(InvoiceFilterDTO filterDTO);

    long filterCount(InvoiceFilterDTO filterDTO);

    InvoiceInfoDTO show(long invoiceId , long storeId , Status status);

    InvoiceInfoDTO getByOrderIdAndStatusAndStoreId(long orderId , Status status  , long storeId);

    List<InvoiceInfoDTO> list(Status status , long storeId , int page , int size);

    List<InvoiceListDTO>  listToJson(Status status, long storeId, int page, int size);

    List<InvoiceInfoDTO> listTopReceivable(Status status , long storeId , int page , int size);

    long countlist(Status status , long storeId);

    double getTotalReceivableByStoreInfoAndStatus(long storeInfoId , Status status);

    List<Double> getTotalSellOfYearByStore(long storeId, String year);

    List<InvoiceInfoDTO> getAllByStatusAndBuyerAndStoreInfo(Status status , long clientId , long storeId , int page , int size);

    List<InvoiceInfoDTO> getAllReceivableByStatusAndBuyerAndStoreInfo(Status status , long clientId , long storeId , int page , int size);

    long countAllByStatusAndBuyerAndStoreInfo(Status status , long clientId , long storeId);

    List<InvoiceInfoDTO> getAllByStatusAndStoreInfoAndInvoiceDateBetween(Status status , long storeId , Date from , Date to , int page , int size);

    long countAllByStatusAndStoreInfoAndInvoiceDateBetween(Status status , long storeId , Date from , Date to);

    void cancel(long invoiceId, String note , long createdById);
}
