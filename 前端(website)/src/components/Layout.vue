<template>
<!--  :class="{loginBG:!onboard}" -->
<div class="layout" :class="{loginBG:!onboard}">
  <!-- <div> -->
  <div v-if="onboard">
    <v-header :name='userSysInfo.name' @onDoExit='doExit' />
    <Menu mode="horizontal" active-key="1">
      <div class="layout-nav">
        <!-- ↓ iview导航样式所需name-->
        <span v-for='items in navItems'>
                    <Menu-item
                     v-for="(item,index) in items.subTitle"
                      v-if='userSysInfo.level<=item.level && item.main==true && userSysInfo.level!=item.intercept'
                     @click.native="linkTo(item)">
                      {{ item.title }}
                    </Menu-item>
                  </span>
      </div>
    </Menu>
    <div class="layout-content">
      <Row>
        <i-col span="5">
          <Menu class='nav' active-key="1-2" width="auto">
            <Submenu v-if='userSysInfo.level<=items.level' v-for="items in navItems" :name="items.title" :key="items.title">
              <template slot="title">
                {{ items.title }}
              </template>
                                                                    <!-- ↓ iview导航样式所需name-->
                <Menu-item v-if='userSysInfo.level<=item.level  && userSysInfo.level != item.intercept'
                 v-for="(item,index) in items.subTitle" :key="index"
                 @click.native="linkTo(item)"
                 :class="{itemondisplay:nowUrl==item.url }">
                    {{ item.title }}
                </Menu-item>
                            <!-- <Menu-item v-if='userSysInfo.level<=item.level'
                             v-for="(item,index) in items.subTitle" :name="index" 取消name属性，用自定义样式替代
                             @click.native="linkTo(item.url)"
                             :class="{itemondisplay:nowUrl==item.url }">
                                {{ item.title }}
                            </Menu-item> -->
            </Submenu>
          </Menu>
        </i-col>
                  <i-col span="19">
                      <div class="layout-content-main">
                        <keep-alive>
                          <router-view :userInfo="userSysInfo" :eventList="eventList" :contextList="contextList"></router-view>
                        </keep-alive>
                      </div>
                  </i-col>
              </Row>
          </div>

          <div class="layout-copy">
              2012-2017 &copy; CDTU
          </div>
      </div>

      <div v-else>
      <!-- <div v-if='false'> -->
        <v-login @onDoLogin='doLogin'/>
      </div>
    </div>
</template>

<script>
import ROUTE from '@/assets/ROUTE'
import navData from '@/assets/navData.js'
import Header from '@/components/header.vue'
import Login from '@/components/login.vue'
var intervali = ''
export default {
  name: 'Layout',
  data: function() {
    return {
      onboard: false,
      userSysInfo: {},
      mainItems: navData.mainItems,
      navItems: navData.navItems,
      nowUrl: this.$route.path,

      eventList: [],
      contextList: []
    }
  },
  mounted() {
    setInterval(this.polling,10000)
    this.getnewe();
    this.getnewc();
  },
  components: {
    'v-header': Header,
    'v-login': Login
  },
  methods: {
    doLogin(userInfo) {
      this.userSysInfo = userInfo
      this.onboard = true
    },
    unloadReq() {
      // this.$http.post(
      //   'api/infoSearchAndFix/',
      //   {},
      //   {emulateJSON: false}
      // )
      // .then(function(res){})
      // .catch(function(err){})
      // alert('退出')
    },
    doExit() {
      this.userSysInfo.name = ''
      var d = new Date();
      d.setTime(d.getTime() - 10000);
      document.cookie = 'userInfo' + '=1; expires=' + d.toGMTString();
      this.onboard = false
      // this.$http.post(
      //   'api/exit',
      //   {
      //     username:
      //   },
      //   {emulateJSON: true}
      // ).then(function(){})
      // .catch(function(){})
    },
    linkTo(item) {
      this.$router.push({
        name: item.name,
        params: {
          user: this.userSysInfo
        }
      })
      this.nowUrl = item.url
    },
    isOnDisplay(url) {
      return url == this.$route.path
    },
    setTimeGetNew(){
    },
    getnewe() {
      this.$http.post(
          ROUTE.toReplace+'/infoSearchAndFix/getNewEvent', {
            uId: this.userSysInfo.uId
          }, {
            emulateJSON: true
          }
        )
        .then(function(res) {
          this.eventList = res.data.eventList
          for (var i = 0; i < res.data.eventList.length; i++) {
            this.eventList[i].startTime = this.datestyle(this.eventList[i].startTime)
          }
        })
        .catch(function(err) {
          this.$Message.error('系统连接失败!getnewe' + err)
        })



    },
    polling(){
      // 18/3/24 添加 /////////////////////////////
      if(this.userSysInfo.name=='')
      return
      /////////////////////////////////////////////
      this.$http.post(
         ROUTE.toReplace+'/workOfEvent/polling',
        {
          uId: this.userSysInfo.uId
        },
        {emulateJSON: true}
      )
      .then(res=>{
        //新回复
        if(res.data.newReply == 1){
          this.$Notice.open({
            title: '您收到一条新回复',
            desc: '请进入"事件列表"查看处理',
            duration: 0,
          })
        }
        //新定向通知
        if(res.data.newPotNotice == 1){
          this.$Notice.open({
            title: '您收到一条新的个人通知',
            desc: '请进入"我的通知"查看处理',
            duration: 0,
          })
        }
        //新部门通知
        if(res.data.newDepNotice == 1){
          this.$Notice.open({
            title: '您收到一条新的部门通知',
            desc: '请进入"部门通知"查看处理',
            duration: 0,
          })
          this.getnewc()
        }
        //新隐患通知
        if(res.data.newEveNotice == 1){
          this.$Notice.open({
            title: '您收到一条新的隐患信息',
            desc: '请进入"隐患列表"查看处理',
            duration: 0,
          })
          this.getnewe()
        }
      })
      .catch()
    },
    isnewe() {

      this.$http.post(

          ROUTE.toReplace+'/infoSearchAndFix/isNewEvent', {
            uId: this.userSysInfo.uId,
            eventDate: this.eventList[0].startTime,
          }, {
            emulateJSON: true
          }
        )
        .then(function(res) {
          if (res.data.isNew == true) {
            this.$Notice.warning({
              title: '有新的隐患信息',
              desc: '请尽快查看处理',
              duration: 0,
            });


            this.getnewe();
          } else {}
        })
        .catch(function(err) {
          this.$Message.error('系统连接失败!isnewe' + err)
        })

    },
    getnewc() {

      this.$http.post(
          ROUTE.toReplace+'/infoSearchAndFix/getNewContext', {
            depId: this.userSysInfo.depId
          }, {
            emulateJSON: true
          }
        )
        .then(function(res) {

          this.contextList = res.data.contextList
          for (var j = 0; j < res.data.contextList.length; j++) {
            this.contextList[j].time = this.datestyle(this.contextList[j].time) ////////////////////////
          }
        })
        .catch(function(err) {

          this.$Message.error('系统连接失败!getnewc' + err)
        })

    },
    // isnewc() {
    //
    //   this.$http.post(
    //       'api/infoSearchAndFix/isNewContext', {
    //         contextDate: this.contextList[0].time,
    //         depId: this.userSysInfo.depId,
    //
    //       }, {
    //         emulateJSON: true
    //       }
    //     )
    //     .then(function(res) {
    //       this.isNew = res.data.isNew
    //       if (res.data.isNew == true) {
    //         this.$Notice.warning({
    //           title: '有新的安全公告',
    //           desc: '请尽快查看处理',
    //           duration: 0
    //
    //         });
    //
    //         this.getnewc();
    //       } else {}
    //     })
    //     .catch(function(err) {
    //       this.$Message.error('系统连接失败!isnewc' + err)
    //     })
    //
    // },
    datestyle(time) {
      let YYYY = 2000 + time.year % 100;
      let MM = time.month + 1;
      let dd = time.date;
      let HH = time.hours;
      let mm = time.minutes;
      let ss = time.seconds;
      return YYYY + "-" + MM + "-" + dd + " " + HH + ":" + mm + ":" + ss
    }
  }
}
</script>

<style scoped>
.layout {
  position: absolute;
  min-height: 100%;
  min-width: 100%;
  overflow: auto;
  background: #f5f7f9;
}

.loginBG {
  position: absolute;
  min-height: 100%;
  min-width: 100%;
  background: url('../assets/timg.jpg');
  background-size: cover;
}

.layout-logo {
  width: 100px;
  height: 30px;
  background: #5b6270;
  border-radius: 3px;
  float: left;
  position: relative;
  top: 15px;
  left: 20px;
}

.layout-nav {
  margin: 0 auto;
  width: 600px;
}

.layout-assistant {
  width: 300px;
  margin: 0 auto;
  height: inherit;
}

.layout-breadcrumb {
  padding: 10px 15px 0;
}

.layout-content {
  min-height: 200px;
  margin: 15px;
  overflow: hidden;
  background: #fff;
  border-radius: 4px;
}

.layout-content-main {
  padding: 10px;
}

.layout-copy {
  text-align: center;
  padding: 10px 0 20px;
  color: #9ea7b4;
}

.nav a {
  width: 100%;
  height: 100%;
  color: gray;
}

.itemondisplay {
  width: 100%;
  background: rgba(244, 244, 244, 0.5);
  font-weight: bold;
  color: rgb(0, 95, 182);
  border-left: 5px solid rgb(0, 95, 182);
}
</style>
