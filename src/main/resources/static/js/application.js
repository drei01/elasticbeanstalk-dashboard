var ApplicationModel = function (url,awsRegions){
		var self = this;
		self.subscription;
		self.region = ko.observable('eu-west-1');//default
		self.regions = ko.observableArray(awsRegions);
		self.items = ko.observableArray();
		self.lastUpdate = ko.observable();
		self.initialised = ko.observable(false);
		
		var socket = new SockJS('/elasticbeanstalk');
		socket.onmessage = function(e) {
			self.buildTemplate(e.data);
		};
		
		//subscribe to region changes to push them through to the server
		self.region.subscribe(function(region) {
			self.initialised(false);
			self.items.removeAll();
		    $.post('region',{region:region.name},function(){
		    	console.log('new region set: '+region.name);
		    });
		});
		   
		self.init = function(){
			$.post('region',{region:self.region()},function(){});
		};
	
		self.buildTemplate = function (messages){
			try {
            	self.initialised(true);
            	
            	self.lastUpdate(moment().calendar());
            	
            	messages = $.parseJSON(messages);

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
		    
		 };
	};