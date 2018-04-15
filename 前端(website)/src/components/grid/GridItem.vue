<template>
  <div class="">
      <i-button v-for="(grid,index) in grids" :key="index" class="botton"
      @click='clickEventSets(grid)'
      :class="{active:grid.number==mark}">
        <div class="">
          {{ grid.name }}
        </div>
      </i-button>
      <!-- {{grids}} -->
      <div class="block-grids">
        <GridItem v-if="distance>0&&subs!=''"
          :grids='subs' :distanceFromParNode='distance-1'
          @currentArea='currentArea'
          @added='reloadSubArea'/>
      </div>
  </div>
</template>
<script>
import GridItem from '@/components/grid/GridItem'
import ROUTE from '@/assets/ROUTE'
export default {
  name: 'GridItem',
  props: ['grids','distanceFromParNode'],
  components: {
    GridItem
  },
  methods: {
    clickEventSets(grid){
      this.mark = grid.number
      this.distance = 1
      this.loadSubArea(grid)

      this.gridKey = grid
    },
    toReload(operation) {
      if(operation=='子区域添加'){
        this.loadSubArea(this.gridKey)
      }
      else{
        this.$emit('added')
      }
    },
    reloadSubArea(){
      this.loadSubArea(this.gridKey)
    },
    currentArea(grid){
      this.$emit('currentArea',grid)
    },
    loadSubArea(grid){
      // this.subs = [
      //   { name: '计算机工程' },
      //   { name: '电子' },
      //   { name: '通信' }
      // ]
      // this.$emit('currentArea',grid)
      this.$http.post(
        ROUTE.toReplace+'/infoSearchAndFix/loadSubArea',
        {
          areId: grid.areId,
          uId: grid.uId
        },
        {emulateJSON: true}
      )
      .then(function(res){
        this.subs = res.data.subAreaList
        grid.admin = res.data.user
        grid.it = this
        this.$emit('currentArea',grid)
      })
      .catch(function(err){
        this.$Message.error('系统连接失败!'+err)
      })
    }
  },

  data (){
    return {
      subs: '',
      mark: '',
      gridKey: '',
      cnt: 0,
      distance: ''
    }
  }
}
</script>
<style lang="css" scoped>
.block-grids{
  padding: 20px 0px 0px 0px;
}
.botton{
  margin: 5px 2px;
}
</style>
