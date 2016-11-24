/*!
 * jQuery ComboTree Plugin 
 * Author:  Erhan FIRAT
 * Mail:    erhanfirat@gmail.com
 * Licensed under the MIT license
 */


;(function ($, window, document, undefined) {

    // Create the defaults once
    var comboTreePlugin = 'comboTree',
        defaults = {
            source: [],
            isMultiple: false
        };

    // The actual plugin constructor
    function ComboTree(element, options) {
        this.elemInput = element;
        this._elemInput = $(element);

        this.options = $.extend({}, defaults, options);

        this._defaults = defaults;
        this._name = comboTreePlugin;

        this.init();
    }

    ComboTree.prototype.init = function () {
        // Setting Doms
        this.comboTreeId = 'comboTree';

        if (this._elemInput.attr('id') === undefined)
            this._elemInput.attr('id', this.comboTreeId + 'Input');
        this.elemInputId = this._elemInput.attr('id');


        //添加了外面一层的大wapper，涵盖input和按钮和dropdown
        this._elemInput.wrap('<div id="' + this.comboTreeId + 'Wrapper" class="comboTreeWrapper"></div>');
        //给input添加div
        this._elemInput.wrap('<div id="' + this.comboTreeId + 'InputWrapper" class="comboTreeInputWrapper"></div>');
        this._elemWrapper = $('#' + this.comboTreeId + 'Wrapper');

        this._combox = $('#justAnInputBox');
        this._elemArrowSpan = $('<span id="arrowWrapper" class="arrowWrapper" ' +
         'style="position: absolute; top: 0px; height: 22px; width: 22px; right: 0px; ' +
         'border-left: 1px solid rgb(204, 204, 204);"><span id ="arrow" class="arrow" ' +
         'style="width: 0px; height: 0px; position: absolute; top: 8px; border-left: ' +
         '7px solid transparent; border-right: 7px solid transparent; left: 3px; ' +
         'border-top: 8px solid rgb(100, 100, 100);"></span></span>')
         this._elemInput.after(this._elemArrowSpan);
        //增加一个隐藏的与，用来存储选择的结果
        this._eleHiddenSeleItem = $('<input type="hidden" id="selectedItem" >');
        this._elemArrowSpan.after(this._eleHiddenSeleItem);
        this._elemArrowWrapper = $('#arrowWrapper');
        this._elemArrow = $('#arrow');
        this._elemWrapper.css({width: this._elemInput.outerWidth()});
        //添加dropdownArea
        this._elemWrapper.append('<div id="' + this.comboTreeId + 'DropDownContainer" class="comboTreeDropDownContainer"><div class="comboTreeDropDownContent"></div>');

        // DORP DOWN AREA
        this._elemDropDownContainer = $('#' + this.comboTreeId + 'DropDownContainer');
        //填充内容

        this._elemDropDownContainer.html(this.createSourceHTML());
        //得到所有的li
        this._elemItems = this._elemDropDownContainer.find('li');
        this._elemItemsTitle = this._elemDropDownContainer.find('span.comboTreeItemTitle');

        // VARIABLES
        this._selectedItem = {};
        this._selectedItems = [];
        this._selectedItems_big = [];
        this._selectedItem_medium = [];
        this.selectedItem_small = [];

        this.bindings();
    };


    // *********************************
    // SOURCES CODES
    // *********************************

    ComboTree.prototype.removeSourceHTML = function () {
        this._elemDropDownContainer.html('');
    };

    ComboTree.prototype.createSourceHTML = function () {
        var htmlText = this.createSourceSubItemsHTML(this.options.source);
        return htmlText;
    };

    ComboTree.prototype.createSourceSubItemsHTML = function (subItems) {
        var subItemsHtml = '<UL class="BigUL">';

        for (var i = 0; i < subItems.length; i++) {
            subItemsHtml += this.createBigItemHTML(subItems[i]);
        }
        subItemsHtml += '</UL>'
        return subItemsHtml;
    }

    ComboTree.prototype.createMediumHTML = function (sourceItem) {
        var itemHtml = "",

        //是否含有小分类
            isThereSmall = sourceItem.hasOwnProperty("small");

        itemHtml = '<LI class="ComboTreeItem' + (isThereSmall ? 'Parent' : 'Chlid') + '"> ';

        if (isThereSmall)
            itemHtml += '<span class="comboTreeParentPlus"><img src="/Cherry/images/angledown.png" height="10" width="10"></img></span></span>';

        if (this.options.isMultiple)
            itemHtml += '<span data-id="' + sourceItem.categoryMediumId + '" class="comboTreeItemTitle"><input type="checkbox">' + sourceItem.primaryCategoryMedium + '</span>';
        else
            itemHtml += '<span data-id="' + sourceItem.categoryMediumId + '" class="comboTreeItemTitle">' + sourceItem.primaryCategoryMedium + '</span>';

        if (isThereSmall) {
            itemHtml += '<UL class="SmallUL">';
            //这里需要循环的
            for (var j = 0; j < sourceItem.small.length; j++) {
                itemHtml += this.createSmallHTML(sourceItem.small[j]);
            }
            itemHtml += "</UL>";
        }
        itemHtml += '</LI>';
        return itemHtml;
    }

    ComboTree.prototype.createSmallHTML = function (sourceItem) {
        var itemHtml = "",

            itemHtml = '<LI class="ComboTreeItem' + 'Chlid' + '"> ';


        if (this.options.isMultiple)
            itemHtml += '<span data-id="' + sourceItem.categorySmallId + '" class="comboTreeItemTitle"><input type="checkbox">' + sourceItem.primaryCategorySmall + '</span>';
        else
            itemHtml += '<span data-id="' + sourceItem.categorySmallId + '" class="comboTreeItemTitle">' + sourceItem.primaryCategorySmall + '</span>';

        itemHtml += '</LI>';
        return itemHtml;
    }

    ComboTree.prototype.createBigItemHTML = function (sourceItem) {
        var itemHtml = "",
        //是否含有中分类
            isThereMedium = sourceItem.hasOwnProperty("medium");
        itemHtml = '<LI class="ComboTreeItem' + (isThereMedium ? 'Parent' : 'Chlid') + '"> ';

        if (isThereMedium)
            itemHtml += '<span class="comboTreeParentPlus"><img src="/Cherry/images/angledown.png" height="10" width="10"></img></span>';

        if (this.options.isMultiple)
            itemHtml += '<span data-id="' + sourceItem.categoryBigId + '" class="comboTreeItemTitle"><input type="checkbox">' + sourceItem.primaryCategoryBig + '</span>';
        else
            itemHtml += '<span data-id="' + sourceItem.categoryBigId + '" class="comboTreeItemTitle">' + sourceItem.primaryCategoryBig + '</span>';
        if (isThereMedium) {
            //这里需要循环的
            itemHtml += '<UL class="MediumUL">';
            for (var i = 0; i < sourceItem.medium.length; i++) {
                itemHtml += this.createMediumHTML(sourceItem.medium[i]);
            }
            itemHtml += "</UL>";
        }
        itemHtml += '</LI>';
        return itemHtml;
    };


    // BINDINGS
    // *****************************
    ComboTree.prototype.bindings = function () {
        var _this = this;

        this._elemArrow.on('click', function (e) {
            e.stopPropagation();
            _this.toggleDropDown();
        });
        this._elemInput.on('click', function (e) {
            e.stopPropagation();
            if (!_this._elemDropDownContainer.is(':visible'))
                _this.toggleDropDown();
        });
        this._elemItems.on('click', function (e) {
            e.stopPropagation();
            if ($(this).hasClass('ComboTreeItemParent')) {
                _this.toggleSelectionTree(this);
            }
        });
        this._elemItemsTitle.on('click', function (e) {
            e.stopPropagation();
            if (_this.options.isMultiple)
                _this.multiItemClick(this);
            else
                _this.singleItemClick(this);
        });

        // KEY BINDINGS
        this._elemInput.on('keyup', function (e) {
            e.stopPropagation();

            switch (e.keyCode) {
                case 27:
                    _this.closeDropDownMenu();
                    break;
                case 13:
                case 39:
                case 37:
                case 40:
                case 38:
                    e.preventDefault();
                    break;
                default:
                    if (!_this.options.isMultiple)
                        _this.filterDropDownMenu();
                    break;
            }
        });
        this._elemInput.on('keydown', function (e) {
            e.stopPropagation();

            switch (e.keyCode) {
                case 9:
                    _this.closeDropDownMenu();
                    break;
                case 40:
                case 38:
                    e.preventDefault();
                    _this.dropDownInputKeyControl(e.keyCode - 39);
                    break;
                case 37:
                case 39:
                    e.preventDefault();
                    _this.dropDownInputKeyToggleTreeControl(e.keyCode - 38);
                    break;
                case 13:
                    if (_this.options.isMultiple)
                        _this.multiItemClick(_this._elemHoveredItem);
                    else
                        _this.singleItemClick(_this._elemHoveredItem);
                    e.preventDefault();
                    break;
                default:
                    if (_this.options.isMultiple)
                        e.preventDefault();
            }
        });
        // ON FOCUS OUT CLOSE DROPDOWN
        $(document).on('mouseup.' + _this.comboTreeId, function (e) {
            if (!_this._elemWrapper.is(e.target) && _this._elemWrapper.has(e.target).length === 0 && _this._elemDropDownContainer.is(':visible'))
                _this.closeDropDownMenu();
        });
    };


    // EVENTS HERE
    // ****************************

    // DropDown Menu Open/Close
    ComboTree.prototype.toggleDropDown = function () {
        this._elemDropDownContainer.slideToggle(50);
        this._elemInput.focus();
    };
    ComboTree.prototype.closeDropDownMenu = function () {
        this._elemDropDownContainer.slideUp(50);
    };
    // Selection Tree Open/Close
    ComboTree.prototype.toggleSelectionTree = function (item, direction) {
        var subMenu = $(item).children('ul')[0];
        if (direction === undefined) {
            if ($(subMenu).is(':visible'))
                $(item).children('span.comboTreeParentPlus').html("<img src='/Cherry/images/angleright.png' height='10' width='10'></img>");
            else
                $(item).children('span.comboTreeParentPlus').html("<img src='/Cherry/images/angledown.png' height='10' width='10'></img>");

            $(subMenu).slideToggle(50);
        }
        else if (direction == 1 && !$(subMenu).is(':visible')) {
            $(item).children('span.comboTreeParentPlus').html("<img src='/Cherry/images/angledown.png' height='10' width='10'></img>");
            $(subMenu).slideDown(50);
        }
        else if (direction == -1) {
            if ($(subMenu).is(':visible')) {
                $(item).children('span.comboTreeParentPlus').html("<img src='/Cherry/images/angleright.png' height='10' width='10'></img>");
                $(subMenu).slideUp(50);
            }
            else {
                this.dropDownMenuHoverToParentItem(item);
            }
        }

    };


    // SELECTION FUNCTIONS
    // *****************************
    ComboTree.prototype.singleItemClick = function (ctItem) {
        this._selectedItem = {
            id: $(ctItem).attr("data-id"),
            title: $(ctItem).text()
        };

        this.refreshInputVal();
        this.closeDropDownMenu();
    };
    ComboTree.prototype.multiItemClick = function (ctItem) {
        this._selectedItem = {
            id: $(ctItem).attr("data-id"),
            title: $(ctItem).text()
        };
        //是否选中了，下标
        var index = this.isItemInArray(this._selectedItem, this._selectedItems);
        //得到选择item所在的li
        var ctItemParent = $(ctItem).parent("li");
        if (index){
            this._selectedItems.splice(parseInt(index), 1);
            $(ctItem).find("input").prop('checked', false);
            //先得到ul,有可能是第三，第二也有可能是第一层的
            var firstUl = ctItemParent.parent("ul");
            alert(firstUl.val());
            var attClass = firstUl.attr("class");
            if(attClass == 'SmallUL'){
                //得到小层的兄弟
                var smallLi = ctItemParent.siblings("li");
                //得到中层li
                var mediumLi = firstUl.parent("li");
                //得到中层ul
                var mediumUl = mediumLi.parent("ul");
                //得到大层li
                var bigLi = mediumUl.parent("li");
                //得到中层span,大层span
                var meduiumSpan = $(mediumLi.children("span")[1]);
                var bigSpan = $(bigLi.children("span")[1]);
                //得到中的index
                this._mediumSelected = {
                    id: $(meduiumSpan).attr("data-id"),
                    title: $(meduiumSpan).text()
                };
                this._bigSelected = {
                    id: $(bigSpan).attr("data-id"),
                    title: $(bigSpan).text()
                };

                var mediumIndex = this.isItemInArray(this._mediumSelected, this._selectedItems);
                var bigIndex = this.isItemInArray(this._bigSelected, this._selectedItems);
                //看看兄弟下的checkbox有没有选中的
                var i = 0;
                for(i =0 ;i < smallLi.length ;i++){
                    //得到span,input
                    var smallSpan = $($(smallLi[i]).children("span")[0]);
                    var smallInput = $(smallSpan.children("input")[0]);
                    if(smallInput.prop("checked") == true){
                        //半选中状态加上
                        $(meduiumSpan.children("input")[0]) .prop("indeterminate",false);
                        break;
                    }
                }
                if(i == smallLi.length){
                    //如果没有的话，要将中层的input去除选中
                    $(meduiumSpan.children("input")[0]).prop('checked', false);
                    //半选中状态去除
                    $(meduiumSpan.children("input")[0]) .prop("indeterminate",false);
                    this._selectedItems.splice(parseInt(mediumIndex), 1);
                    //得到中层的li兄弟
                    var mediumLiSib = mediumLi.siblings("li");
                    //中层兄弟是否有选中的
                    var j = 0;
                    for(j =0 ;j < mediumLiSib.length ;j++){
                        //得到中层span,input
                        var mediumSpan = $($(mediumLiSib[j]).children("span")[1]);
                        var mediumInput = $(mediumSpan.children("input")[0]);
                        if(mediumInput.prop("checked") == true){
                            //大层的半选中状态增加
                            $(bigSpan.children("input")[0]).prop('indeterminate',true);
                            break;
                        }
                    }
                    //如果没有选中的话，
                    if(j == mediumLiSib.length){
                        //得到大层的input
                        $(bigSpan.children("input")[0]).prop('checked', false);
                        this._selectedItems.splice(parseInt(bigIndex), 1);
                        //大层的半选中状态去除
                        $(bigSpan.children("input")[0]).prop('indeterminate',false);
                    }
                }
            }else if(attClass = 'MediumUL'){
                //得到中层所有的兄弟
                var mediumLiSib = ctItemParent.siblings("li");
                //得到大层li，大层span
                var bigLi = firstUl.parent("li");
                var bigSpan =  $(bigLi.children("span")[1]);
                //得到大层的index
                this._bigSelected = {
                    id: $(bigSpan).attr("data-id"),
                    title: $(bigSpan).text()
                };
                var bigIndex = this.isItemInArray(this._bigSelected, this._selectedItems);
                //便利兄弟
                var k = 0;
                for(k = 0; k < mediumLiSib.length;k++){
                    //得到中层span,input
                    var mediumSpan = $($(mediumLiSib[k]).children("span")[1]);
                    var mediumInput = $(mediumSpan.children("input")[0]);
                    if(mediumInput.prop("checked") == true){
                        //大层的半选中状态加上
                        $(bigSpan.children("input")[0]).prop('indeterminate',true);
                        break;
                    }
                }
                //如果没有选中的话，
                if(k == mediumLiSib.length){
                    //得到大层的input
                    $(bigSpan.children("input")[0]).prop('checked', false);
                    this._selectedItems.splice(parseInt(bigIndex), 1);
                    //大层的半选中状态去除
                    $(bigSpan.children("input")[0]).prop('indeterminate',false);
                }
            }else if(attClass = 'BigUL'){
            }
            //这里需要进行批量操作，将下面所有的孩子都去除选中
            if (ctItemParent.hasClass('ComboTreeItemParent')){
                //得到所有的孩子
                var childrens = ctItemParent.find(".comboTreeItemTitle");
                for(var i = 1;i < childrens.length;i++){
                    //得到title下面的checkbox
                    var childrenTitle = $(childrens[i]);
                    this._selectedChildren = {
                        id: $(childrenTitle).attr("data-id"),
                        title: $(childrenTitle).text()
                    };
                    var indexItem = this.isItemInArray(this._selectedChildren, this._selectedItems);
                    var checkbo = childrenTitle.children("input[type='checkbox']");
                    this._selectedItems.splice(parseInt(indexItem), 1);
                    //alert(checkbo);
                    checkbo.attr("checked",false);
                    checkbo.prop("indeterminate", false);
                }
            }/*else{
                this._selectedItems.splice(parseInt(index), 1);
                $(ctItem).find("input").prop('checked', false);
            }*/
        }
        else {
            //没选中的话，这里要进行选中，要将其父类都要进行选中
            $(ctItem).find("input").prop('checked', true);
            this._selectedItems.push(this._selectedItem);
            //先得到ul,有可能是第三，第二也有可能是第一层的
            var firstUl = ctItemParent.parent("ul");
            var attClass = firstUl.attr("class");
            if(attClass == 'SmallUL'){
                //得到中层li
                var mediumLi = firstUl.parent("li");
                //得到中层ul
                var mediumUl = mediumLi.parent("ul");
                //得到大层li
                var bigLi = mediumUl.parent("li");
                //得到中层span,大层span
                var meduiumSpan = $(mediumLi.children("span")[1]);
                var bigSpan = $(bigLi.children("span")[1]);
                //得到自己所有兄弟的状态
                var smallSiblings = ctItemParent.siblings("li");
                var i = 0;
                var flag = 0;
                for(i =0 ;i < smallSiblings.length ;i++){
                    //得到span,input
                    var smallSpan = $($(smallSiblings[i]).children("span")[0]);
                    var smallInput = $(smallSpan.children("input")[0]);
                    if(smallInput.prop("checked") == false){
                        flag = 1;
                        break;
                    }
                }
                //找到中层input,并将其独自选中
                $(meduiumSpan.children("input")[0]).prop('checked',true);
                //找到大层input,并将其独自选中
                $(bigSpan.children("input")[0]).prop('checked',true);
                if(flag){
                    //存在未选中的话,中层半选中
                    $(meduiumSpan.children("input")[0]).prop("indeterminate", true);
                    //大层半选中
                    $(bigSpan.children("input")[0]).prop("indeterminate", true);
                }
                else{
                    $(meduiumSpan.children("input")[0]).prop("indeterminate", false);
                    //查看中层是否全选中，首先找到中层所有的兄弟
                    //得到中层的li兄弟
                    var mediumLiSib = mediumLi.siblings("li");
                    //中层兄弟是否有未选中的
                    var j = 0;
                    var flag_1 = 0;
                    for(j =0 ;j < mediumLiSib.length ;j++){
                        //得到中层span,input
                        var mediumSpan = $($(mediumLiSib[j]).children("span")[1]);
                        var mediumInput = $(mediumSpan.children("input")[0]);
                        if(mediumInput.prop("checked") == false){
                            flag_1 = 1;
                            break;
                        }
                    }
                    if(flag_1){
                        //如果有未选中的话
                        //大层半选中
                        $(bigSpan.children("input")[0]).prop("indeterminate", true);
                    }
                    else{
                        $(bigSpan.children("input")[0]).prop("indeterminate", false);
                    }
                }
                //判断中，大分类是否在选中当中
                this._selectedMediumSpan = {
                    id: $(meduiumSpan).attr("data-id"),
                    title: $(meduiumSpan).text()
                };
                this._selectedBigSpan = {
                    id: $(bigSpan).attr("data-id"),
                    title: $(bigSpan).text()
                };
                var mediumIndex = this.isItemInArray(this._selectedMediumSpan, this._selectedItems);
                var bigIndex = this.isItemInArray(this._selectedBigSpan, this._selectedItems);
                if(!mediumIndex){
                    //如果没选中,将中层span加入进去
                    this._selectedItems.push(this._selectedMediumSpan);
                }if(!bigIndex){
                    //如果没选中,将大层span加入进去
                    this._selectedItems.push(this._selectedBigSpan);
                }
            }else if(attClass == 'MediumUL'){
                //得到大层的li
                var bigLi = firstUl.parent("li");
                //得到大层的span
                var bigSpan = $(bigLi.children("span")[1]);
                //找到大层input,并将其独自选中
                $(bigSpan.children("input")[0]).prop('checked',true);
                //得到自己所有兄弟的状态
                var mediumSiblings = ctItemParent.siblings("li");
                var j = 0;
                var flag_2 = 0;
                for(j =0 ;j < mediumSiblings.length ;j++){
                    //得到中层span,input
                    var mediumSpan = $($(mediumSiblings[j]).children("span")[1]);
                    var mediumInput = $(mediumSpan.children("input")[0]);
                    if(mediumInput.prop("checked") == false){
                        flag_2 = 1;
                        break;
                    }
                }if(flag_2){
                    //如果有未选中的话
                    //大层半选中
                    $(bigSpan.children("input")[0]).prop("indeterminate", true);
                }else{
                    $(bigSpan.children("input")[0]).prop("indeterminate", false);
                }
                this._selectedBigSpan = {
                    id: $(bigSpan).attr("data-id"),
                    title: $(bigSpan).text()
                };
                //大层是否选中
                var bigIndex = this.isItemInArray(this._selectedBigSpan, this._selectedItems);
                if(!bigIndex){
                    //如果没选中,将大层span加入进去
                    this._selectedItems.push(this._selectedBigSpan);
                }
            }else if(attClass == 'BigUL'){
                //如果是大层直接选中就可以了
            }
            //这里需要进行批量操作，将下面所有的孩子都选中
            if (ctItemParent.hasClass('ComboTreeItemParent')){
                //得到所有的孩子
                var childrens = ctItemParent.find(".comboTreeItemTitle");
                for(var i = 0;i < childrens.length;i++){
                    //得到title下面的checkbox
                    var childrenTitle = $(childrens[i]);
                    this._selectedChildren = {
                        id: $(childrenTitle).attr("data-id"),
                        title: $(childrenTitle).text()
                    };
                    var indexItem = this.isItemInArray(this._selectedChildren, this._selectedItems);
                    //得到title下面的checkbox
                    var childrenTitle = $(childrens[i]);
                    var checkbo = childrenTitle.children("input[type='checkbox']");
                    this._selectedItem_1 = {
                        id: $(childrenTitle).attr("data-id"),
                        title: $(childrenTitle).text()
                    };
                    var indexTemp = this.isItemInArray(this._selectedItem_1, this._selectedItems);
                    if(!indexTemp){
                        //如果不在的话，再推入
                        this._selectedItems.push(this._selectedItem_1);
                    }
                    //alert(checkbo);
                    checkbo.attr("checked",true);
                }
            }/*else{
                this._selectedItems.push(this._selectedItem);
                $(ctItem).find("input").prop('checked', true);
            }*/
        }
        //页面产品信息的刷新，发送请求传递参数
        this.refreshInputVal();
        datatableFilter(this._eleHiddenSeleItem,22);
    };

    ComboTree.prototype.isItemInArray = function (item, arr) {

        for (var i = 0; i < arr.length; i++)
            if (item.id == arr[i].id && item.title == arr[i].title)
                return i + "";

        return false;
    }


    ComboTree.prototype.refreshInputVal = function () {
        var tmpTitle = "";
            for (var i = 0; i < this._selectedItems.length; i++) {
                tmpTitle += this._selectedItems[i].id;
                if (i < this._selectedItems.length - 1)
                    tmpTitle += ", ";
            }
        alert("length is:"+this._selectedItems.length+"????id is:"+tmpTitle);
        this._eleHiddenSeleItem.val(tmpTitle);
    }

    ComboTree.prototype.dropDownMenuHover = function (itemSpan, withScroll) {
        this._elemItems.find('span.comboTreeItemHover').removeClass('comboTreeItemHover');
        $(itemSpan).addClass('comboTreeItemHover');
        this._elemHoveredItem = $(itemSpan);
        if (withScroll)
            this.dropDownScrollToHoveredItem(this._elemHoveredItem);
    }

    ComboTree.prototype.dropDownScrollToHoveredItem = function (itemSpan) {
        var curScroll = this._elemDropDownContainer.scrollTop();
        this._elemDropDownContainer.scrollTop(curScroll + $(itemSpan).parent().position().top - 80);
    }

    ComboTree.prototype.dropDownMenuHoverToParentItem = function (item) {
        var parentSpanItem = $($(item).parents('li.ComboTreeItemParent')[0]).children("span.comboTreeItemTitle");
        if (parentSpanItem.length)
            this.dropDownMenuHover(parentSpanItem, true);
        else
            this.dropDownMenuHover(this._elemItemsTitle[0], true);
    }

    ComboTree.prototype.dropDownInputKeyToggleTreeControl = function (direction) {
        var item = this._elemHoveredItem;
        if ($(item).parent('li').hasClass('ComboTreeItemParent'))
            this.toggleSelectionTree($(item).parent('li'), direction);
        else if (direction == -1)
            this.dropDownMenuHoverToParentItem(item);
    }

    ComboTree.prototype.dropDownInputKeyControl = function (step) {
        if (!this._elemDropDownContainer.is(":visible"))
            this.toggleDropDown();

        var list = this._elemItems.find("span.comboTreeItemTitle:visible");
        i = this._elemHoveredItem ? list.index(this._elemHoveredItem) + step : 0;
        i = (list.length + i) % list.length;

        this.dropDownMenuHover(list[i], true);
    },

        ComboTree.prototype.filterDropDownMenu = function () {
            var searchText = this._elemInput.val();
            if (searchText != "") {
                this._elemItemsTitle.hide();
                this._elemItemsTitle.siblings("span.comboTreeParentPlus").hide();
                list = this._elemItems.find("span:icontains('" + this._elemInput.val() + "')").each(function (i, elem) {
                    $(this).show();
                    $(this).siblings("span.comboTreeParentPlus").show();
                });
            }
            else {
                this._elemItemsTitle.show();
                this._elemItemsTitle.siblings("span.comboTreeParentPlus").show();
            }
        }

    // Retuns Array (multiple), Integer (single), or False (No choice)
    ComboTree.prototype.getSelectedItemsId = function () {
        if (this.options.isMultiple && this._selectedItems.length > 0) {
            var tmpArr = [];
            for (i = 0; i < this._selectedItems.length; i++)
                tmpArr.push(this._selectedItems[i].id);

            return tmpArr;
        }
        else if (!this.options.isMultiple && this._selectedItem.hasOwnProperty('id')) {
            return this._selectedItem.id;
        }
        return false;
    }

    // Retuns Array (multiple), Integer (single), or False (No choice)
    ComboTree.prototype.getSelectedItemsTitle = function () {
        if (this.options.isMultiple && this._selectedItems.length > 0) {
            var tmpArr = [];
            for (i = 0; i < this._selectedItems.length; i++)
                tmpArr.push(this._selectedItems[i].title);

            return tmpArr;
        }
        else if (!this.options.isMultiple && this._selectedItem.hasOwnProperty('id')) {
            return this._selectedItem.title;
        }
        return false;
    }


    ComboTree.prototype.unbind = function () {
        this._elemArrow.off('click');
        this._elemInput.off('click');
        this._elemItems.off('click');
        this._elemItemsTitle.off('click');
        this._elemItemsTitle.off("mousemove");
        this._elemInput.off('keyup');
        this._elemInput.off('keydown');
        this._elemInput.off('mouseup.' + this.comboTreeId);
        $(document).off('mouseup.' + this.comboTreeId);
    }

    ComboTree.prototype.destroy = function () {
        this.unbind();
        this._elemWrapper.before(this._elemInput);
        this._elemWrapper.remove();
        this._elemInput.removeData('plugin_' + comboTreePlugin);
    }


    $.fn[comboTreePlugin] = function (options) {
        var ctArr = [];
        this.each(function () {
            if (!$.data(this, 'plugin_' + comboTreePlugin)) {
                $.data(this, 'plugin_' + comboTreePlugin, new ComboTree(this, options));
                ctArr.push($(this).data()['plugin_' + comboTreePlugin]);
            }
        });

        if (this.length == 1)
            return ctArr[0];
        else
            return ctArr;
    }

})(jQuery, window, document);




