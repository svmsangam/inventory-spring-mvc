let predictRequest;
$(document).ready(function(){
    $('.predictStock').click(function(){
        thispointer = $(this);
        let row = thispointer.closest('tr'); // Find the closest parent <tr> element
        let productId = row.find('.productId').val(); // Find the .productId input in the same row
        let productName = row.find('.productName').val(); // Find the .productName input in the same row
        let productCategory = row.find('.productCategory').val();
        predictRequest = $.ajax({
            type: "POST",
            url: $('.predictStock').attr('url'),
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            data : {
                productId:productId,
                productName: productName,
                category : productCategory
            },
            dataType : 'json',
            timeout : 30000,
            beforeSend : function(xhr) {
                abortQuickSaleChargeRequest();
                thispointer.prop("disabled", true);
                blockUiZ(2001);
            },
            success : function(data) {
                console.log(data.detail);
                createModal(data.detail,data.message);
                $.unblockUI();
                thispointer.prop("disabled", false);
            },

            error : function(xhr, exception) {
                console.log(exception);
                thispointer.prop("disabled", false);

            }
        });

    });


});

function abortQuickSaleChargeRequest() {
    if (predictRequest !== undefined) {
        predictRequest.abort();
    }
}
function createModal(data,message) {
    // Create a Bootstrap v3.3 modal
    $('#myModal').remove();
    let modalHtml = '<div id="myModal" class="modal fade" role="dialog">';
    modalHtml += '<div class="modal-dialog">';
    modalHtml += '<div class="modal-content">';

    // Modal Header
    modalHtml += '<div class="modal-header">';
    modalHtml += '<button type="button" class="close" data-dismiss="modal">&times;</button>';
    modalHtml += '<h4 class="modal-title">'+message+'</h4>'; // Add your modal title here
    modalHtml += '</div>'; // End modal-header

    // Modal Body
    modalHtml += '<div class="modal-body">';
    modalHtml += '<ul>';
    for (let brand in data) {
        modalHtml += '<li>' + brand + ': ' + data[brand] + '</li>';
    }
    modalHtml += '</ul>';
    modalHtml += '</div>'; // End modal-body

    // Modal Footer (Optional)
    modalHtml += '<div class="modal-footer">';
    modalHtml += '<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>';
    modalHtml += '</div>'; // End modal-footer

    modalHtml += '</div>'; // End modal-content
    modalHtml += '</div>'; // End modal-dialog
    modalHtml += '</div>'; // End modal

    // Add the modal HTML to the body
    $('body').append(modalHtml);

    // Show the Bootstrap modal
    $('#myModal').modal('show');
}
