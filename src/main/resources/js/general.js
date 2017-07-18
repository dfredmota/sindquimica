function callPickerDate() {
	console.log('chama datepicker date');
	// Date range picker
	$('.datePicker').datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
		//startDate : '+0d'
	});
}
function callPickerDateRange() {
	console.log('chama datepicker dateNasc');
	// Date range picker
	$('.dateRangePicker').datepicker({
		format : 'dd/mm/yyyy',
		autoclose : true,
		//endDate : '-1d'
	})
}

function extenderName(){
    this.cfg.highlighter = {
	   show: true,
	   tooltipAxes: 'y',
	   useAxesFormatters: false,
	   tooltipFormatString: 'R$ %.2f',
	   tooltipContentEditor:tooltipContentEditor
    };
}

function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {
    // display series_label with x and y values value
    return plot.series[seriesIndex]["label"] + ", " + str;
    
}

function fixHeigth() {
    //Get window height and the wrapper height
    var neg = $('.main-header').outerHeight() + $('.main-footer').outerHeight();
    var window_height = $( document ).height();//$(window).height();
    var sidebar_height = $(".sidebar").height();
    //Set the min-height of the content and sidebar based on the
    //the height of the document.
    if ($("body").hasClass("fixed")) {
      $(".content-wrapper, .right-side").css('min-height', window_height - $('.main-footer').outerHeight());
    } else {
      var postSetWidth;
      if (window_height >= sidebar_height) {
        $(".content-wrapper, .right-side").css('min-height', window_height - neg);
        postSetWidth = window_height - neg;
      } else {
        $(".content-wrapper, .right-side").css('min-height', sidebar_height);
        postSetWidth = sidebar_height;
      }

      //Fix for the control sidebar height
      var controlSidebar = $($.AdminLTE.options.controlSidebarOptions.selector);
      if (typeof controlSidebar !== "undefined") {
        if (controlSidebar.height() > postSetWidth)
          $(".content-wrapper, .right-side").css('min-height', controlSidebar.height());
      }

    }
}

function closeDialog(dialog){
	$('#'.concat(dialog)).modal('hide');
}
function openDialog(dialog){
	$('#'.concat(dialog)).modal('show');
}