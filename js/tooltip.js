angular
.module('TooltipApp', [])
.directive('tooltip', TooltipDirective);

function TooltipDirective($timeout) {
  return { link: postLink };
  
  function postLink (scope, element, attr) {
    attachTooltip(attr.tooltip);
    
    var tooltipSize = getTooltipSize();
    var radius = Math.ceil(Math.sqrt(Math.pow(tooltipSize.width / 2, 2) + Math.pow(tooltipSize.height, 2)));
    var vPadding = (radius - tooltipSize.height);
    var hPadding = Math.ceil((radius * 2 - tooltipSize.width) / 2);
    var $inner = angular.element(element[0].querySelector('.tooltip-inner'));
    var $content = $inner.children('.content');
    
    hideTooltip(true);
    
    element.on('mouseenter', showTooltip);
    element.on('mouseleave', hideTooltip.bind(null, false));
    
    function hideTooltip(skip) {
      if (skip) {
        $content.addClass('no-transition');
        $inner.addClass('no-transition');
      }
      $content.css({
        position: 'absolute',
        opacity: 0
      });
      $inner.css({ opacity: 0, paddingLeft: 0, paddingRight: 0, paddingBottom: 0, paddingTop: 0, width: 0, height: 0 });
      if (skip) {
        $content.removeClass('no-transition');
        $inner.removeClass('no-transition');
      }
    }
    
    function showTooltip() {
      $content.css({
        opacity: 1
      });
      $inner.css({
        paddingLeft: hPadding + 'px',
        paddingRight: hPadding + 'px',
        width: tooltipSize.width + 'px',
        height: tooltipSize.height + 'px',
        opacity: 1
      });
      switch (attr.tooltipPosition) {
        case 'top':
          $inner.css({
            paddingTop: vPadding + 'px'
          });
          break;
        default:  
          $inner.css({ paddingBottom: vPadding + 'px', });
      }
    }
    
    function getTooltipSize() {
      var content = element[0].querySelector('.content');
      return { width: content.offsetWidth, height: content.offsetHeight };
    }
    function attachTooltip(text) {
      element.append('\
        <div class="tooltip">\
          <div class="tooltip-inner">\
            <div class="content">' + text + '</div>\
          </div>\
        </div>');
    }
  }
}