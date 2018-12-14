app.controller('searchController', function($scope,$location, searchService) {

    $scope.searchMap = {'keywords':'','category':'', 'brand':'', 'spec':{},'price':'','pageNo':1,'pageSize':30,'sort':'','sortField':'' };
    $scope.search=function() {

        searchService.search( $scope.searchMap).success(function (response) {

            $scope.resultMap = response;
            buildPageLabel();//调用
        });
    }

    // 根据页码查询
    $scope.queryByPage=function(pageNo){
        //页码验证
        if(pageNo<1 || pageNo>$scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo=pageNo;
        $scope.search();

    }

    //构建分页标签
    buildPageLabel=function(){

        //构建分页栏
        $scope.pageLabel=[];
        var firstPage=1;//开始页码
        var lastPage=$scope.resultMap.totalPages;//截止页码

        if($scope.resultMap.totalPages>5){// 总页码数大于5
            // 判断当前页

            if($scope.searchMap.pageNo<=3){//如果当前页码小于等于3 ，显示前5页
                lastPage=5;

            }else if( $scope.searchMap.pageNo>= $scope.resultMap.totalPages-2 ){//显示后5页
                firstPage=$scope.resultMap.totalPages-4;

            }else{  //显示以当前页为中心的5页
                firstPage=$scope.searchMap.pageNo-2;
                lastPage=$scope.searchMap.pageNo+2;
            }
        }
        //循环产生页码标签
        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    }

    // 添加搜索项
    $scope.addSearchItem=function(key ,value){

        // 如果页面点击的是分类或者品牌
        if(key=='category'||key=='brand'|| key=='price'){
            $scope.searchMap[key]=value;
        }else{
            $scope.searchMap.spec[key]=value;
        }
        $scope.search();//执行搜索
    }


    // 撤销搜索项--- 和上面相反

    $scope.removeSearchItem=function(key){
        if('category'==key||'brand'==key || key=='price') {
            $scope.searchMap[key]='';
        }else {
            delete $scope.searchMap.spec[key];
        }

        $scope.search();//执行搜索
    }


    // 按升序降序排序
    $scope.sortSearch=function(sortField ,sort ){

        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;
        $scope.search();
    }

    // 判断关键字是不是品牌 解决隐藏品牌列表的功能
    $scope.keywordsIsBrand=function(){

        // 循环集合看看是否包含关键字的子字符串
        for(var i=0;i<$scope.resultMap.brandList.length;i++){
            if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
                return true;
            }
        }
        return false;
    }

    // 加载从首页传递过来的搜索关键字
    $scope.loadkeywords=function(){
        $scope.searchMap.keywords= $location.search()['keywords'];
        $scope.search();

    }
});