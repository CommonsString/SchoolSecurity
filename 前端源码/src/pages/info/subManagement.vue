<template>
  <div class="block-table">
    <i-button type="primary" name="button"
    class="button-edit"
    @click='submit'>
      提交
    </i-button>
    <div class="title-block">
      下属管理
    </div>
    <!-- <h3>subs: {{ subs }}</h3> -->
    <div class="block">
     <Transfer ref="transfer" :uId="uId"
     @transferMounted="getUserList"/>
    </div>
  </div>
</template>
<script>
import ROUTE from '@/assets/ROUTE'
import Transfer from '@/components/transfer/Transfer'
export default {
  name: "",
  data(){
    return{
      uId: this.$route.params.uId,
      subs: []
    }
  },
  props: ['userInfo'],
  methods: {
    getUserList() {
      this.$http.post(
        ROUTE.toReplace+'/infoSearchAndFix/getUserList',
        {
          uId: this.userInfo.uId,
          depId: this.userInfo.depId
        },
        {emulateJSON: true}
      )
      .then(function(res){
        this.$refs.transfer.data3 = this.transformData(res.data.userList)
        this.$refs.transfer.getTargetKeys()
      })
      .catch(function(err){
        this.$Message.error('系统连接失败!'+err)
      })
    },
    transformData(List){
      let userList = new Array()
      for(var i=0;i<List.length;i++){
        let user = {}
        user.key = List[i].uId
        user.uId = List[i].uId
        user.name = List[i].name
        user.parId = List[i].parId
        userList.push(user)
      }
      return userList
    },
    submit(){
      this.subs=this.$refs.transfer.targetKeys3.toString()
      this.$http.post(
        ROUTE.toReplace+'/infoSearchAndFix/submitUserList',
        {
          uId: this.userInfo.uId,
          uIdList: this.subs
        },
        {emulateJSON: true}
      )
      .then(function(res){
        if(res.data.state==true){
          this.$Message.success(res.data.message)
        }
        else{
          this.$Message.error(res.data.message)
        }
      })
      .catch(function(err){
        this.$Message.error('系统连接失败!'+err)
      })
    }
  },
  components: {
    Transfer
  }
}
</script>
<style lang="css" scoped>
.title-block{
  padding: 10px 0px;
  border-bottom: 1px dotted rgb(218, 218, 218);
}
.block{
  padding: 10px 30px;
}
.block-transfer{
  margin: 30px auto;
  width: 690px;
}
.button-edit{
  float: right;
  margin-left: 10px;
}
</style>
