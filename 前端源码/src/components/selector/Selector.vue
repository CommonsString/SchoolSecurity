<template>
  <div class="block-selector"
  :class="{pointer: !this.disabled, notAllowed: this.disabled}"
  @click.stop='clickEventSets'>
    {{ item.name }}
    <!-- {{ itemList }} -->
    <div style="float: right;">
      <Icon type="arrow-down-b" color="gray" v-if="!show"></Icon>
      <Icon type="arrow-up-b" color="gray" v-if="show"></Icon>
    </div>
    <div v-show="show" class="block-list">
      <div class="search" @click.stop>
        <input type="text" v-model="searchInfo" placeholder="查找..">
      </div>
      <ul>
        <li v-for="(item,index) in itemList" :key="index"
        v-show="searchInfo==''||match(item.name)"
        @click='alterItem(item)'
        class="item-list">{{ item.name }}</li>
      </ul>
    </div>
  </div>
</template>
<script>
export default {
  name: "",
  props:['disabled'],//API: 控制是否禁用
  methods: {
    clickEventSets() {
      if(this.disabled)
      return
      this.show=!this.show
    },
    alterItem(item){  //API: 触发选项变更事件
      this.item=item
      this.$emit('changeItem',item)
    },
    match(name){
      return name.indexOf(this.searchInfo)>=0
    }
  },
  data(){
    return{
      show: false,
      item: {},       //API: 被选中项 通过ref.item改变
      itemList: {},    //API: 选项列表 通过ref.itemList改变
      searchInfo: ""
    }
  }
}
</script>
<style lang="css" scoped>
.block-selector{
  position: relative;
  padding: 0px 8px;
  width: 100%;
  height: 33px;
  border: 1px solid rgb(212, 207, 207);
  border-radius: 4px;
  z-index: 99;
}
.pointer{
  cursor: pointer;
}
.notAllowed{
  cursor: not-allowed;
}
.block-selector:hover{
  border: 1px solid rgb(4, 150, 222);
}
.block-list{
  position: absolute;
  left: 0px;
  top: 32px;
  background: white;
  width: 100%;
  border-radius: 4px;
  box-shadow: 0px 0px 10px -3px black;
  max-height: 200px;
  overflow: auto;
}
.search{
  position: relative;
  width: 100%;
}
.search input{
  box-sizing: border-box;
  width: 100%; height: 40px;
  padding: 5px;

}
.item-list{
  width: 100%;
  padding: 5px;
}
.item-list:hover{
  background: rgb(230,230,230);
}
</style>
