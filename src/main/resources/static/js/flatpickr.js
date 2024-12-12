//datepicker
$("input.datepicker").flatpickr({
	mode: "range",
	conjunction: "-",
//    defaultDate: [
//        new Date(new Date().setDate(new Date().getDate() - 14)), // 開始日期：兩週前
//        new Date() // 結束日期：今天
//    ],
    dateFormat: "Y-m-d",   
    maxDate: "today",
	minDate:"2024/1/1"  
});
