app.controller('contentController',function($scope,contentService){
	
	$scope.contentList=[];//广告列表
	
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(
			function(response){
				$scope.contentList[categoryId]=response;
			}
		);		
	}

	// 首页搜索跳转到用户搜索页
	$scope.search=function(){
        location.href="http://localhost:9004/search.html#?keywords="+$scope.keywords;
	}
	
});