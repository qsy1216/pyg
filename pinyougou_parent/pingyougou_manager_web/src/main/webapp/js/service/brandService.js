// 品牌服务
		app.service("brandService",function($http){
			//查询全部
			this.findAll=function(){
				return $http.get('../brand/findAll.do');
			}
			// 分页
			this.findPage=function(page,size){
				return $http.get('../brand/findPage.do?page='+page +'&size='+size);
			}
			
			//根据id查询单个
			this.findOne=function(id){
				return $http.get('../brand/findOne.do?id='+id);
			}
			
			//添加方法
			this.add=function(entity){
				return $http.post('../brand/add.do',entity);
			}
			
			//修改方法
			this.update=function(entity){
				return $http.post('../brand/update.do',entity);
			}
			//删除方法
			this.dele=function(ids){
				return $http.get('../brand/delete.do?ids='+ids);
			}
			
			//条件分页查询
			this.search=function(page,size,searchEntity){
				return $http.post('../brand/search.do?page='+page +'&size='+size, searchEntity);
			}
			
			//下拉数据列表
			this.selectOptionList=function(){
				return $http.get('../brand/selectOptionList.do');
			}
			
		}); 