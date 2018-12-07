//登录控制层
app.controller('indexController',function($scope,$controller ,loginService){
	// 读取登录人名称
	$scope.showLoginName=function(){
		
		loginService.loginName().success(
				function(response){
					$scope.loginName=response.loginName;
					
				}
		
		);
	}
	
});