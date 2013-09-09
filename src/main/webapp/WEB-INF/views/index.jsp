<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>AWS Dashboard</title>
    <meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate, max-age=0">
    <link href="assets/css/portfolio.css" rel="stylesheet">
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link href="assets/css/bootstrap-responsive.css" rel="stylesheet">

    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <![endif]-->

  </head>
  <body>

    <div class="container-fluid">
      <div id="heading" class="masthead row-fluid">
      	<div class="span12">
      		<h3 class="span6 muted">ElasticBeanstalk Environments</h3>
	        <div class="span6">
	          <span class="pull-right text-info">
	          	<strong>Region</strong>
	          	<select data-bind="options: regions, value: region"></select>
	          </span>
	        </div>
        </div>
      </div>
      <div id="main-content row-fluid">
      	<div>
      		<small>
      			Last updated:
      			<em data-bind="text: lastUpdate"></em>
      		</small>
      	</div>
        <table class="table table-striped span12" id="no-more-tables">
          <thead>
            <tr>
              <th>Application Name</th>
              <th>Health</th>
              <th>Status</th>
              <th>Version</th>
              <th>All Versions</th>
            </tr>
          </thead>
          <tbody data-bind="foreach: items">
            <tr>
              <td data-title="Application Name" class="titled">
              	(<span data-bind="text:applicationName"></span>) <span data-bind="text: environmentName"></span><br/>
              	<a href="#" target="_blank" data-bind="attr: {href: 'http://'+cname} , text: cname"></a>
              </td>
              <td data-title="Health" class="titled" data-bind="text: health , style: {color: health == 'Green' ? 'green' : 'red'}"></td>
              <td data-title="Status" class="titled" data-bind="text: status , style: {color: status != 'Ready' ? 'red' : ''}"></td>
              <td data-title="Version" class="titled">
              	<span data-bind="text: versionLabel"></span>
              </td>
              <td>
              	<div class="checkbox-wrapper">
	              	<input type="checkbox" data-bind="checked:showVersions" name="all-versions" id="all-versions">
	              	<label for="all-versions"><span></span>all versions</label>
              	</div>
              	<div class="popover visible" data-bind="visible:showVersions">
		            <div class="arrow"></div>
		            <h3 class="popover-title">Versions</h3>
		            <div class="popover-content">
		            	<table class="table table-striped">
		            		<tbody data-bind="foreach: versions">
		            			<tr>
		            				<td data-bind="text: versionLabel"></td>
		            				<td data-bind="text: date"></td>
		            			</tr>
		            		</tbody>
		              	</table>
		            </div>
		        </div>
              </td>
             </tr>
             <tr data-bind="with: envResources">
             	<td></td>
             	<td>
             		<span class="label label-info">
             			<img src="<c:url value="/assets/img/SVG/Amazon_EC2_Instance.svg"/>" alt="Instances" width="20"/>
             			Instances: 
             			<span data-bind="text:instances.length"></span>
             		</span>
             	</td>
             	<td>
             		<span class="label label-info">
	             		<img src="<c:url value="/assets/img/SVG/Auto_Scaling.svg"/>" alt="AutoScaling Groups" width="20"/>
					    Auto Scaling Groups:
					    <span data-bind="text:autoScalingGroups.length"></span>
				    </span>
             	</td>
             	<td>
             		<span class="label label-info">
	             		<img src="<c:url value="/assets/img/SVG/Elastic_Load_Balancing.svg"/>" alt="Load Balancers" width="20"/>
					    LoadBalancers:
					    <span data-bind="text:loadBalancers.length"></span>
				    </span>
             	</td>
             </tr>
          </tbody>
          <tfoot>
          	<tr data-bind="visible: items().length == 0">
          		<td colspan="5" data-bind="visible: !initialised()" style="display:none;">
          			<div class="alert">
          				Please wait..
          			</div>
          		</td>
          		
          		<td colspan="5" data-bind="visible: initialised()" style="display:none;">
          			<div class="alert alert-info">
          				No applications
          			</div>
          		</td>
          	</tr>
          </tfoot>
        </table>
      </div>
    </div>
    
    <script>
	    /*!
	     * $script.js Async loader & dependency manager
	     * https://github.com/ded/script.js
	     * (c) Dustin Diaz 2013
	     * License: MIT
	     */
	   (function(e,t,n){typeof module!="undefined"&&module.exports?module.exports=n():typeof define=="function"&&define.amd?define(n):t[e]=n()})("$script",this,function(){function v(e,t){for(var n=0,r=e.length;n<r;++n)if(!t(e[n]))return f;return 1}function m(e,t){v(e,function(e){return!t(e)})}function g(e,t,a){function d(e){return e.call?e():r[e]}function b(){if(!--p){r[h]=1,c&&c();for(var e in s)v(e.split("|"),d)&&!m(s[e],d)&&(s[e]=[])}}e=e[l]?e:[e];var f=t&&t.call,c=f?t:a,h=f?e.join(""):t,p=e.length;return setTimeout(function(){m(e,function(e){if(e===null)return b();if(u[e])return h&&(i[h]=1),u[e]==2&&b();u[e]=1,h&&(i[h]=1),y(!n.test(e)&&o?o+e+".js":e,b)})},0),g}function y(n,r){var i=e.createElement("script"),s=f;i.onload=i.onerror=i[d]=function(){if(i[h]&&!/^c|loade/.test(i[h])||s)return;i.onload=i[d]=null,s=1,u[n]=2,r()},i.async=1,i.src=n,t.insertBefore(i,t.firstChild)}var e=document,t=e.getElementsByTagName("head")[0],n=/^https?:\/\//,r={},i={},s={},o,u={},a="string",f=!1,l="push",c="DOMContentLoaded",h="readyState",p="addEventListener",d="onreadystatechange";return!e[h]&&e[p]&&(e[p](c,function b(){e.removeEventListener(c,b,f),e[h]="complete"},f),e[h]="loading"),g.get=y,g.order=function(e,t,n){(function r(i){i=e.shift(),e.length?g(i,r):g(i,t,n)})()},g.path=function(e){o=e},g.ready=function(e,t,n){e=e[l]?e:[e];var i=[];return!m(e,function(e){r[e]||i[l](e)})&&v(e,function(e){return r[e]})?t():!function(e){s[e]=s[e]||[],s[e][l](t),n&&n(i)}(e.join("|")),g},g.done=function(e){g([null],e)},g})
	</script>
    
    <script>
    	$script.path('assets/js/');
    	$script('jquery-1.8.3',function(){
    		$script(['bootstrap','knockout-2.3.0','moment','jquery.atmosphere'],function(){
    			$script(['knockout-mapping','application'],'bundle');
    		});
    	});
    </script>
    
    <script>
	    $script.ready('bundle', function() {
	    	var appModel = new ApplicationModel('<c:url value="/elasticbeanstalk"/>',
	    			[
						<c:forEach var="region" items="${regions}" varStatus="status">
							'${region.name}'
							${not status.last ? ',' : ''}
						</c:forEach>
	    			]);
	    	appModel.init();
	        ko.applyBindings(appModel);
	    });
    </script>

  </body>
</html>
