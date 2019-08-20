var today = new Date();
var todayString = dateFns.format(today,"DD/MM/YYYY");
var yesterday = dateFns.addDays(today,-1);
var yesterdayString = dateFns.format(yesterday,"DD/MM/YYYY");

var data_form = new Vue(    {
    el: '#data-form',
    data: {
        uiData: {
            date: todayString,
            nameOrVehicleNumber: "",
            vehicleNumber: "",
            depositAmount:0.0,
            transactionType:1,
            moreData: [{
                vendor: "",
                vendorAmount: "",
                batteryType: "",
                batteryTypeAmount: "",
                serialNumberAndCode: "",
                serviceSerialNumberCode: "",
                serviceBatteryCharges: 0,
                isSerBat:false,
                chargingCharges:0.0
            }],
            message: ""
        }
    },
    methods: {
        dataForm: function (event) {
            var $this = this;
            var dataDetail = [];
            $(this.uiData.moreData).each(function (k, value) {
                dataDetail.push({
                    "batteryVendor": value.vendor,
                    "batteryType": value.batteryType,
                    "serialNumberCode": value.serialNumberAndCode,
                    "serviceCharges": value.serviceBatteryCharges,
                    "serviceSerialNumberCode": value.serviceSerialNumberCode,
                    chargingCharges:value.chargingCharges
                });
            });
            var data = {
                "name": $this.uiData.nameOrVehicleNumber,
                "vehicleNumber": $this.uiData.vehicleNumber,
                "depositAmount": $this.uiData.depositAmount,
                "transactionType": $this.uiData.transactionType,
                maintenanceDetailData: dataDetail,
                checkInDate: $this.uiData.date,
            };
            axios.post("/api/maintenance", data).then(function (res) {
                if (res.status === 200) {
                    swal({
                        title: "Good job!",
                        text: "Data saved successfully.",
                        icon: "Success",
                        button: "Ok"
                    });
                    $this.uiData = {
                        date: todayString,
                        nameOrVehicleNumber: "",
                        transactionType:1,
                        depositAmount:0.0,
                        moreData: [{
                            vendor: "",
                            vendorAmount: "",
                            batteryType: "",
                            batteryTypeAmount: "",
                            serialNumberAndCode: "",
                            serviceBatteryCharges: 0,
                            isSerBat:false,
                            chargingCharges:0.0
                        }],
                        message: ""
                    };
                } else {
                    swal({
                        title: "Opps!",
                        text: "Unable to save data.",
                        icon: "Error",
                        button: "Ok"
                    });
                }
            });
        },
        addDetails: function (e) {
            this.uiData.moreData.push({
                vendor: "",
                vendorAmount: "",
                batteryType: "",
                batteryTypeAmount: "",
                serialNumberAndCode: "",
                serviceBatteryCharges: 0,
                isSerBat:false,
                chargingCharges:0.0
            });
        },
        removeElement: function (index) {
            this.uiData.moreData.splice(index, 1);
        }
    }
});
//data_form.serialNumber();

var data_view = new Vue({
    el: '#viewRecord',
    data: {
        gridData: [],
        searchText: "",
        startTime:yesterdayString,
        endTime:todayString,
        startDate:yesterday,
        endDate:today,
        isDate:true
    },
    methods: {
        getDataFromServer: function () {
            var $this = this;
            // axios.get("/api/maintenance/").then(function (res) {
            //     // console.log(res);
            //     if (res.status === 200) {
            //         $this.gridData = res.data;
            //     }
            // });
            this.onSearch();
        },
        action: function (dataID, nameClick, e) {
            // var dataAction = e.currentTarget.getAttribute("data-action");
            // var dataID = e.currentTarget.getAttribute("data-id");
            // console.log("action which is taken",dataAction);

            switch (nameClick) {
                case "nameClick":
                    this.loadModelData(dataID, exampleModalLong);
                    break;
                case "checkOutClick":
                    this.loadModelData(dataID, checkoutModel);
                    break;
                default:
                    break;
            }
        },
        loadModelData: function (id, model) {
            var $this = this;
            axios.get("/api/maintenance/" + id).then(function (res) {
                console.log(res);
                if (res.status === 200) {
                    console.log("data...", res.data);
                    model.loadModelData(res.data);
                }
            });
        },
        onSearch: function () {

            var $this = this;
            var url = "/api/maintenance/search?text="
            + this.searchText;
            if (this.isDate){
                url +="&startDate="+this.startTime+"&endDate="+this.endTime;
            }
            axios.get(url).then(function (res) {
                if (res.status === 200) {
                    $this.gridData = res.data;
                }
            });
        }
    }
});

var exampleModalLong = new Vue({
    el: "#exampleModalLong",
    data: {
        modelData: {
            "id": 0,
            "name": "",
            "maintenanceDetailData": [
                {
                    "id": 0,
                    "batteryVendor": "",
                    "batteryType": "",
                    "serialNumberCode": "",
                    "serviceCharges": 0,
                    "maintenanceData": null
                }
            ],
            "batteryInDate": "",
            "batteryOutDate": "",
            "serialNumber": null,
            "checkInDate": "",
            "checkOutDate": null
        }
    },
    methods: {
        loadModelData: function (data) {
            this.modelData = data;
        }
    }
});
var checkoutModel = new Vue({
    el: "#checkout-Model",
    data: {
        modelData: {
            "id": 1,
            "name": "Rahul",
            "vehicleNumber": "9579791608",
            "checkInDate": "13/08/2019",
            "batteryVendor": "Ammron",
            "batteryType": "Li",
            "serialNumberCode": "DFRT457639",
            "serviceCharges": "200.0",
            "maintenanceDetailData": [
                {
                    "id": 1,
                    "batteryVendor": "Ammron",
                    "batteryType": "Li",
                    "serialNumberCode": "DFRT457639",
                    "serviceSerialNumberCode": "SDHFFJ48859",
                    "serviceCharges": 200.0,
                    "currentServiceCharges": 0.0,
                    "batteryStatus": "CHECKIN",
                    "checkInDate": "13/08/2019",
                    "checkOutDate": null,
                    chargingCharges:0.0
                }
            ],
            "depositAmount": 100.0,
            "transactionType": "CASH",
            "accountStatus": "OPEN",
            "checkOutDate": null
        }
    },
    methods: {
        loadModelData: function (data) {
            this.modelData = data;
        },
        checkOutClick:function (data) {
            console.log("id ...",data.id);
            var batteryStatus = data.batteryStatus ==="CHECKIN" ? "CHECKOUT" : "CHECKIN";
            // data.batteryStatus = data.batteryStatus ==="CHECKIN" ? "CHECKOUT" : "CHECKIN";
            var updateBatteryStatus =[
                {
                    "id":data.id,
                    "batteryStatus":batteryStatus
                }
            ];
            axios.post("/api/maintenance/updateBatteryStatus", updateBatteryStatus).then(function (res) {
                if (res.status === 200) {
                    data.batteryStatus = batteryStatus;
                } else {
                    swal({
                        title: "oops!",
                        text: "Something is wrong.",
                        icon: "Error",
                        button: "Ok"
                    });
                }
            });
        },
        printOutClick:function (id){
            printDataView.getPrintData(id);
        }
    }
});
var tabs = new Vue({
    el: "#myTab",
    methods: {
        searchTabClick: function () {
            data_view.getDataFromServer();
        }
    }
});
var printDataView = new Vue({
    el:"#printData",
    data:{
        printData:{
            "name": "",
            "vehicleNumber": "",
            "todayDate": "",
            "receiptNumber": null,
            "maintenanceDetailDataDTOS": [
                {
                    "serialNumberCode": "",
                    "batteryVendor": "",
                    "batteryType": "",
                    "serviceCharges":0 ,
                    "currentServiceCharges": 200,
                    "batteryStatus": "",
                    "checkInDate": "",
                    "checkOutDate": ""
                }
            ]
        }
    },
    methods:{
        getPrintData:function (id) {
            var $this = this;
            axios.get("/api/maintenance/print/"+id).then(function (res) {
                // console.log(res);
                if (res.status === 200) {
                    $this.printData = res.data;
                    window.setTimeout(function () {
                        printJS('printData', 'html');
                    },200);
                }
            });
        }
    }
});
$(document).ready(function () {
    $("#exampleModalLong").on("show.bs.modal", function (e) {

    });
    var nowTemp = new Date();
    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
    // $("#check-in-date").datepicker({
    //     format:"dd/mm/yyyy",
    //     onRender: function (date) {
    //         return date.valueOf() < now.valueOf() ? 'disabled' : '';
    //     }
    // }).on('changeDate', function (ev) {
    //     data_form.uiData.date = $("#check-in-date").val();
    // });

    $("#startTime").datepicker({
        format:"dd/mm/yyyy",
        // onRender: function (date) {
        //     return date.valueOf() < now.valueOf() ? 'disabled' : '';
        // }
    }).on('changeDate', function (ev) {
        data_view.startTime = $("#startTime").val();
        data_view.startDate = ev.date;
        if(data_view.startDate.getTime() > data_view.endDate.getTime()){
            data_view.endDate = dateFns.addDays(data_view.startDate,1)
            data_view.endTime =
                dateFns.format(data_view.endDate,"DD/MM/YYYY");
        }
    });

    $("#endTime").datepicker({
        format:"dd/mm/yyyy",
        // onRender: function (date) {
        //     return date.valueOf() < now.valueOf() ? 'disabled' : '';
        // }
    }).on('changeDate', function (ev) {
        data_view.endTime = $("#endTime").val();
        data_view.endDate = ev.date;
        if(data_view.startDate.getTime() > data_view.endDate.getTime()){
            data_view.startDate = dateFns.addDays(data_view.endDate,-1)
            data_view.startTime =
                dateFns.format(data_view.startDate,"DD/MM/YYYY");
        }
        // data_view.startTime =
        //     dateFns.format(dateFns.addDays(ev.date,-1),"DD/MM/YYYY");
    });

});


