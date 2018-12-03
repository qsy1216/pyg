//登录控制层
app.controller('indexController',function($scope,$controller ,loginService){
	// 读取登录人名称
	$scope.showLoginName=function(){
		
		loginService.sellerId().success(
				function(response){
					$scope.sellerId=response.sellerId;
					
				}
		
		);
	}
	
});