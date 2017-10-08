/**
 * Created by dhiraj on 8/12/17.
 */

//user app start
var userService = new UserService();

$(document).ready(function () {

    $(document).on("click", "#saveuser", function () {

        var user = setUserData();

        var url = $(this).attr("url");

        var pagecontext = $(this).attr("pagecontext");

        userService.save(user, url, pagecontext);
    });


    $(document).on("click", ".addUser", function () {

        userService.clearError();
        userService.clearForm();
    });

    $(document).on("click", ".closeError", function () {
        $(".addError").removeClass("hide").removeClass("show").addClass("hide");
    });
});


function setUserData() {

    var username = $("#inventoryuser").val();
    var password = $("#userpassword").val();
    var repassword = $("#userrepassword").val();
    var userType = $("#userType").val();
    var storeId = $("#storeId").val();

    if (username === undefined) {
        username = "";
    }

    if (password === undefined) {
        password = "";
    }

    if (repassword === undefined) {
        repassword = ""
    }

    if (userType === undefined) {
        userType = "";
    }

    if (storeId === undefined) {
        storeId = 0;
    }

    var user = new User();

    user.inventoryuser = username;
    user.userpassword = password;
    user.userrepassword = repassword;
    user.userType = userType;
    user.storeId = storeId;

    console.log("username : " + username + " pass : " + password + " repass : " + repassword + " userType : " + userType);

    return user;
}
//user app end


//store app start
var storeService = new StoreService();

$(document).ready(function () {

    $(document).on("click", ".savestore", function () {

        var store = setStoreData();

        var url = $(this).attr("url");

        var pagecontext = $(this).attr("pagecontext");

        storeService.save(store, url, pagecontext, true);
    });

    $(document).on("click", ".edit", function () {
        userService.clearError();
        userService.clearForm();
    });

    $(document).on("click", ".updatestore", function () {

        var store = setStoreDataOnUpdate();

        var url = $(this).attr("url");

        storeService.save(store, url, "nn", false);
    });

    $(document).on("click", ".addStore", function () {

        storeService.clearError();
        storeService.clearForm();
    });

    $(document).on("click", ".viewStoreInfo", function () {

        var url = $(this).attr("url");

        console.log("url " + url);

        storeService.getById(url);
    });

    $(document).on("click", ".closeError", function () {
        $(".addError").removeClass("hide").removeClass("show").addClass("hide");
    });
});


function setStoreData() {

    var name = $("#name").val();
    var contact = $("#contact").val();
    var email = $("#email").val();
    var mobile = $("#mobile").val();
    var street = $("#street").val();
    var reg = $("#reg").val();
    var pan = $("#pan").val();
    var cityId = $("#cityId").val();

    if (name === undefined) {
        name = "";
    }

    if (contact === undefined) {
        contact = "";
    }

    if (mobile === undefined) {
        mobile = ""
    }

    if (email === undefined) {
        email = "";
    }

    if (street === undefined) {
        street = "";
    }

    if (reg === undefined) {
        reg = "";
    }

    if (cityId === undefined) {
        cityId = 0
    }

    if (pan === undefined) {
        pan = "";
    }

    var store = new Store();

    store.name = name;
    store.contact = contact;
    store.mobileNumber = mobile;
    store.email = email;
    store.street = street;
    store.regNumber = reg;
    store.panNumber = pan;
    store.cityId = cityId;

    return store;
}


function setStoreDataOnUpdate() {

    var storeId = $("#storeIdEdit").attr("storeId");
    var contact = $("#contactEdit").val();
    var email = $("#emailEdit").val();
    var mobile = $("#mobileEdit").val();
    var street = $("#streetEdit").val();
    var reg = $("#regEdit").val();
    var pan = $("#panEdit").val();

    if (storeId === undefined) {
        storeId = 0;
    }

    if (contact === undefined) {
        contact = "";
    }

    if (mobile === undefined) {
        mobile = ""
    }

    if (email === undefined) {
        email = "";
    }

    if (street === undefined) {
        street = "";
    }

    if (reg === undefined) {
        reg = "";
    }

    if (pan === undefined) {
        pan = "";
    }

    var store = new Store();

    store.storeId = storeId;
    store.contact = contact;
    store.mobileNumber = mobile;
    store.email = email;
    store.street = street;
    store.regNumber = reg;
    store.panNumber = pan;

    return store;
}
//store app end

//sale order app start
var orderInfoService = new OrderInfoService();
$(document).ready(function () {

    $(document).on("change" , ".item" , function () {
        var itemId = $(this).val();

        if (itemId === undefined || itemId === null || 1 > itemId){
            return;
        }else {
            orderInfoService.getItemById(itemId , $(this).attr("url") , $(this));
        }

    });

    $('input[type=radio][name=saleTrack]').change(function() {

        if($(this).val() === undefined || $(this).val() === null){
            return;
        } else if($(this).attr("data-id") === undefined || $(this).attr("data-id") === null || "" === $(this).attr("data-id") || 1 > $(this).attr("data-id")){
            return;
        }else if($(this).attr("url") === undefined || $(this).attr("url") === null || "" === $(this).attr("url")){
            return;
        }else{
            orderInfoService.changeSaleTrack($(this).attr("url") , $(this).val() , $(this).attr("data-id"));
        }

    });

});

//sale order app end

// order colculator start
function calculate(cb) {

    var subtotal = 0;
    $("tr").each(function (index) {

        var quantity = 0;
        var rate = 0;
        var discount = 0;

        if ($(this).find("td:eq(1) > input").val() !== undefined || $(this).find("td:eq(1) > input").val() !== null || "" !== $(this).find("td:eq(1) > input").val() || 0 > $(this).find("td:eq(1) > input").val()) {
            quantity = $(this).find("td:eq(1) > input").val();
        }

        if ($(this).find("td:eq(3) > input").val() !== undefined || $(this).find("td:eq(3) > input").val() !== null || "" !== $(this).find("td:eq(3) > input").val() || 0 > $(this).find("td:eq(3) > input").val()) {
            discount = $(this).find("td:eq(3) > input").val();
        }

        if ($(this).find("td:eq(2) > input").val() !== undefined || $(this).find("td:eq(2) > input").val() !== null || "" !== $(this).find("td:eq(2) > input").val() || 0 > $(this).find("td:eq(2) > input").val()) {
            rate = $(this).find("td:eq(2) > input").val();
        }

        if (quantity !== undefined && rate !== undefined && discount !== undefined) {
            var amount = quantity * rate;
            amount = amount - (amount * (discount / 100));
            subtotal = subtotal + amount;
            $(this).find("td:eq(4) > span").text("").text(amount.toFixed(2));
        }

    });

    return cb(subtotal);

}


function amountUpdate(amount)  {

    if($("#tax").val() !== undefined || $("#tax").val() !== null || "" !== $("#tax").val() || 0 > $("#tax").val()){
        amount = amount + (amount * $("#tax").val() /100);
        $("#total").text("").text(amount.toFixed(2));
        console.log(" amountUpdate  "  + amount);
    }
}

// order colculator end



//invoice app start

$(document).ready(function () {

    $(document).on("click" , ".payment" , function () {

        $(".invoice").removeClass("show").removeClass("hidden").addClass("hidden");
        $(".payment-info").removeClass("show").removeClass("hidden").addClass("show");

    });

    $(document).on("click" , ".back" , function () {

        $(".invoice").removeClass("show").removeClass("hidden").addClass("show");
        $(".payment-info").removeClass("show").removeClass("hidden").addClass("hidden");

    });
});

//invoice app end