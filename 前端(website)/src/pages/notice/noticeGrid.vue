<template>
  <div id="">
    <div class="title-block">
      定向通知
    </div>
<!-- 网格树部分 -->
    <div class="block">
    请选择接收区域：
      <GridItem ref="gridItem" :grids='subs' :distance='1'
      @currentArea='alterCurrentArea'/>
      <div class="upload" @click="linkTo()">
        <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"
        class="icon-upload"></Icon>
        <p>点击开始编辑</p>
      </div>
    </div>
  </div>
</template>
<script>
import ROUTE from '@/assets/ROUTE'
import GridItem from '@/components/grid/GridItem'
export default {
  name: "",
  mounted() {
    this.loadTopArea()
    // setInterval(this.hehe,1000)
  },
  props: ['userInfo'],
  data: () => ({
    subs: [],
    currentArea: {}
  }),
  components: {
    GridItem
  },
  methods: {
    loadTopArea(){
      // this.subs = [
      //   {
      //     name: '成工院'
      //   }
      // ]
      this.$http.post(
        ROUTE.toReplace+'/infoSearchAndFix/loadTopArea',
        {
          uId: this.userInfo.uId
        },
        {emulateJSON: true}
      )
      .then(function(res){
        this.$refs.gridItem.subs=""
        this.subs=res.data.areaList
      })
      .catch(function(res){
        this.$Message.error('系统连接失败!'+err)
      })
    },
    alterCurrentArea(grid){
      this.currentArea=grid
    },
    linkTo(){
      if(this.currentArea.areId==undefined){
        alert('请选择通知的区域')
        return
      }
      let href = ROUTE.location+"/potNotice.jsp?FSEdfsfKfsefseFGfsfNESGfsefNfseFEJS="+this.userInfo.uId
                +"&FSEdawdaKdwadFHNSdawdEUdawHF="+this.currentArea.areId
      let top = (window.screen.availHeight-600)/2
      let left = (window.screen.availWidth-1000)/2
      let location = ', top='+top+', left='+left
      window.open(href,'编辑窗口','width=1000, height=600'+location)
    }
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
.upload{
  width: 400px;
  margin: 40px auto 50px auto;
  padding: 30px 160px;
  border: 1px dashed rgb(177, 177, 177);
  border-radius: 5px;
}
.upload:hover{
  border: 1px dashed rgb(0, 114, 208);
  cursor: pointer;
}
.icon-upload{
  margin: 0px 10px;
}
</style>
