//app.js
App({
  onLaunch: function () {
    //声明一个userinfo对象封装用户的登录信息
    var userInfo = 
    //登录前首先去检查用户状态是否保持，若未保持，则重新登录
    wx.checkSession({
      success:function(){
        //TODO 暂时还没写好干嘛
      },
      fail:function(){
        // 登录
        wx.login({
          success: res => {
            // 获取用户信息
            wx.getSetting({
              success: res => {
                if (res.authSetting['scope.userInfo']) {
                  // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
                  wx.getUserInfo({
                    success: res => {
                      // 可以将 res 发送给后台解码出 unionId
                      this.globalData.userInfo = res.userInfo
                      //封装userinfo对象
                      var userEntity = {
                        "userWxName": res.userInfo.nickName,
                        "gender": res.userInfo.gender,
                        "userLocation": res.userInfo.country + '/' + res.userInfo.province + '/' + res.userInfo.city,
                        "code": res.code
                      };
                      console.debug(userEntity);
                      var path = this.globalData.path;
                      //获取根地址
                      //在这里发送微信后的用户信息到后台服务器
                      wx.request({
                        url: path + 'wx/login',
                        method: 'post',
                        data: JSON.stringify(userEntity),
                        success: function (data) {
                          console.debug(data);
                        }
                      })
                      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                      // 所以此处加入 callback 以防止这种情况
                      if (this.userInfoReadyCallback) {
                        this.userInfoReadyCallback(res)
                      }
                    }
                  })
                }
              }
            })
          }
        })
      }
    }) 
  },
  globalData: {
    userInfo: null,
    path:'http://localhost:8080/'
  }
})