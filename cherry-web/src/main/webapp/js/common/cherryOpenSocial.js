
// 向服务端发送rest请求
function sendRequestToServer(url, method, opt_postParams, opt_callback, opt_excludeSecurityToken) {
    
	opt_postParams = opt_postParams || {};
	
	var makeRequestParams = {
  		'CONTENT_TYPE' : 'JSON',
  		'METHOD' : method,
  		'POST_DATA' : gadgets.json.stringify(opt_postParams)};

	if (!opt_excludeSecurityToken) {
  		url = url + '?st=' + generateSecureToken();
	}

	gadgets.io.makeNonProxiedRequest(url,
  		function(data) {
    		data = data.data;
    		if (opt_callback) {
        		opt_callback(data);
    		}
  		},
  		makeRequestParams,
  		{'Content-Type':'application/javascript'}
	);
}

//生成访问小工具的SecureToken
function generateSecureToken() {
    
    var fields = ['Cherry', 'Cherry', 'Gadget', 'Shindig', 'url', '0', 'default'];
    for (var i = 0; i < fields.length; i++) {
      // escape each field individually, for metachars in URL
      fields[i] = escape(fields[i]);
    }
    return fields.join(':');
}

var callbackAdded = false;
function subscribeRequest(url, opt_callback) {
    // jquery.atmosphere.response
    function callback(response) {
        // Websocket events.
        //$.atmosphere.log('info', ["response.state: " + response.state]);
        //$.atmosphere.log('info', ["response.transport: " + response.transport]);

        detectedTransport = response.transport;
        if (response.transport != 'polling' && response.state != 'connected' && response.state != 'closed') {
            //$.atmosphere.log('info', ["response.responseBody: " + response.responseBody]);
            if (response.status == 200) {
                var data = response.responseBody;
                if (data.length > 0) {
                	if (opt_callback) {
                		opt_callback(data);
            		}
                }
            }
        }
    }

    
    $.atmosphere.subscribe(url, !callbackAdded ? callback : null,
            $.atmosphere.request = { transport: 'websocket',timeout: '1200000',fallbackTransport: 'long-polling',maxRequest: 10000000 });
    callbackAdded = true;
}

function parsingTemplate(temp, params) {
	if(temp) {
		if(params && params.length > 0) {
			for(var i = 0; i < params.length; i++) {
				temp = temp.replace('{'+i+'}', params[i]);
			}
		}
	}
	return temp;
}

function adjustHeight() {
	var ifm = window.frameElement;
    var subWeb = ifm.contentDocument;
    if(ifm != null && subWeb != null) {
    	ifm.height = subWeb.body.scrollHeight;
    }
}