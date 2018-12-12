app.controller('searchController', function($scope, searchService) {

    $scope.searchMap = {'keywords':'','category':'', 'brand':'', 'spec':{}};
    $scope.search=function() {

        searchService.search( $scope.searchMap).success(function (response) {


            $scope.resultMap = response;
            console.log( $scope.resultMap.options);
        });
    }

    // 添加搜索项
    $scope.addSearchItem=function(key ,value){

        // 如果页面点击的是分类或者品牌
        if(key=='category'||key=='brand'){
            $scope.searchMap[key]=value;
        }else{
            $scope.searchMap.spec[key]=value;
        }
        $scope.search();//执行搜索
    }




    // 撤销搜索项--- 和上面相反

    $scope.removeSearchItem=function(key){
        if('category'==key||'brand'==key) {
            $scope.searchMap[key]='';
        }else {
            delete $scope.searchMap.spec[key];
        }

        $scope.search();//执行搜索
    }
});