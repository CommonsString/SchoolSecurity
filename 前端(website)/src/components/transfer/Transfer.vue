<template>
  <div class="">
    <!-- targetKeys:{{ targetKeys3 }}<br>
    data3:{{data3}} -->
    <Transfer class="block-transfer"
       :titles="['未分管','已分管']"
       :data="data3"
       :target-keys="targetKeys3"
       :list-style="listStyle"
       :render-format="render3"
       :operations="['移出','移入']"
       filterable
       @on-change="handleChange3">
       <!-- <div :style="{float: 'right', margin: '5px'}">
           <i-button type="ghost" size="small" @click="reloadMockData">刷新</i-button>
       </div> -->
   </Transfer>
  </div>

</template>
<script>
export default {
  name: "",
  props: ['uId'],
  mounted() {
    this.$emit('transferMounted')
  },
  data(){
    return {
        data3: [],
        targetKeys3: [],
        listStyle: {
            width: '300px',
            height: '380px'
        }
    }
  },
  methods: {
    getTargetKeys () {
        return this.targetKeys3 = this.data3
                .filter((item)=> item.parId!='')
                .map(item => item.key);
    },
    handleChange3 (newTargetKeys) {
        this.targetKeys3 = newTargetKeys;
    },
    render3 (item) {
        return item.name;
    }
    // reloadMockData () {
    //     this.targetKeys3 = this.getTargetKeys();
    // }
  }
}
</script>
<style lang="css" scoped>
</style>
