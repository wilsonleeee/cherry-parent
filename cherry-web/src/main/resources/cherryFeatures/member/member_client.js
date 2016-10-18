/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * @static
 * @namespace Support for basic member capability for gadgets.
 * @name member
 */
gadgets.member = (function() {

    var info = null;
    var loaded = false;
    var attempted = false;
    var baseInfo = null;

// Create and return our public functions
    return {
        setOutcome : function(data, handler) {
            if ( handler === 'silent' ) handler = (function (result) { } );
            if ( handler === undefined ) handler = (function (result) {
                if (result.error) {
                    alert('Error, unable to send outcome to server.');
                }
            } ) ;
            osapi.member.setOutcome({'outcome' : data}).execute(handler);
        },

        setInfo : function(memberinfo) {
            loaded = true;
            attempted = true;
            info = memberinfo.info;
        },
        
        getContextLabel : function() {
            if ( info ) {
               return info.context_label;
            } else {
               return null;
            }
        },
    
        getContextName : function() {
            if ( info ) {
               return info.context_title;
            } else {
               return null;
            }
        },
    
        loadInfo : function(handler) {
            if ( loaded ) return;
            if ( attempted )  return;
            attempted = true;
            osapi.member.getInfo().execute(function(result) {
                loaded = true;
                if (!result.error) {
                    loaded = true;
                    attempted = true;
                    info = result.info;
                }
                handler(result);
            }) ;
        },
        
        setBaseInfo : function(activityInfo) {
            loaded = true;
            attempted = true;
            baseInfo = activityInfo.baseInfo;
        },
        
        getActivityName : function() {
            if ( baseInfo ) {
                return baseInfo.activityName;
             } else {
                return null;
             }
         },
         
         getBrandName : function() {
             if ( baseInfo ) {
                 return baseInfo.brandName;
              } else {
                 return null;
              }
          },
          
          getCreatorName : function() {
              if ( baseInfo ) {
                  return baseInfo.creatorName;
               } else {
                  return null;
               }
           },
           getActivityDesp : function() {
               if ( baseInfo ) {
                   return baseInfo.activityDesp;
                } else {
                   return null;
                }
            },
            getActivityStatus : function() {
                if ( baseInfo ) {
                    return baseInfo.activityStatus;
                 } else {
                    return null;
                 }
             }
    };

})(); 

/* Sample provisioning using the batch pattern

       var batch = osapi.newBatch();
       // ... include other things in the batch
       if ( gadgets.member && osapi.member ) batch.add('memberData', osapi.member.getInfo());
       batch.execute(render);

       function render(data) {

           // ... handle other things in batch
           if ( gadgets.member && osapi.member ) {
               gadgets.member.setInfo(data.memberData);
           }
           // Data is now valid
           alert('Label ' + gadgets.member.getContextLabel());
 
*/

/* Sample provisioning pattern using loadInfo

   gadgets.member.loadInfo(function(result) {
        if (result.error) {
          // Data is not loaded
        } else {
          // Data is loaded
          alert('Label ' + gadgets.member.getContextLabel());
        }
   }) ;
   // At this point the anynch data load will not have completed yet...

*/

/* Sample direct call otu send an outcome with handler

    osapi.member.setOutcome({'outcome' : '123456'}).execute(function (result) {
        if (result.error) {
            alert('Error, unable to send outcome to server.');
        }
    } ) ;

*/
