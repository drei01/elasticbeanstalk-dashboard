var ApplicationModel = function (url,awsRegions){
		var self = this;
		var socket = $.atmosphere;
		self.subscription;
		self.region = ko.observable('eu-west-1');//default
		self.regions = ko.observableArray(awsRegions);
		self.items = ko.observableArray();
		self.lastUpdate = ko.observable();
		self.initialised = ko.observable(false);
		
		self.request = new $.atmosphere.AtmosphereRequest();
		self.request.transport = 'websocket';
		self.request.url = url;
		self.request.contentType = "application/json";
		self.request.fallbackTransport = 'streaming';
		self.request.onMessage = function(response){
			   self.buildTemplate(response);
		};
		
		//subscribe to region changes to push them through to the server
		self.region.subscribe(function(region) {
			self.initialised(false);
			self.items.removeAll();
		    $.post('region',{region:region},function(){
		    	console.log('new region set: '+region);
		    });
		});
		   
		self.init = function(){
			$.post('region',{region:self.region()},function(){
				self.subscription = socket.subscribe(self.request);
		    });
		};
	
		self.buildTemplate = function (response){
			
		     if(response.state = "messageReceived"){
		    	var data = response.responseBody;
		        if (data) {
		            try {
		            	self.initialised(true);
		            	
		            	self.lastUpdate(moment().calendar());
		            	
		                var messages =  $.parseJSON(data);
	
		                $.each(messages,function(key,item){
		                	$.each(item.versions,function(key,version){
		                		version.date = ko.computed(function(){
		                			return moment(item.dateUpdated).format('DD/MM/YYYY h:mm:ss a');
		                		});
		                	});
		                	
		                	var itemSet = false;
		                	$.each(self.items(),function(key,itemInList){
			                	if(itemInList().environmentId == item.environmentId){
			                		ko.mapping.fromJS(item, {}, itemInList);
			                		itemSet = true;
			                		return false;
			                	}
			                });
		                	
		                	if(!itemSet){
		                		item.showVersions = ko.observable(false);
		                		self.items.push(ko.observable(item));
		                	}
						});
	
		            } catch (error) {
		                console.log("An error ocurred: " + error);
		            }
		        } else {
		            console.log("response.responseBody is null - ignoring.");
		        }
		   	}
		    
		 };
	};