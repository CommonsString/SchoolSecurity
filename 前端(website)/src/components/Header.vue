<template>
  <div class='block-header'>
    <div class="mask"></div>
    <div class="block-logo">
      <img class="logo" src="../assets/CDTUlogo.png">
    </div>
    <div class="block-title">
      <div class="title">
        成都工业学院安全稳定网格化管理系统
      </div>
      <div class="block-login">
        <ul>
          <li>{{ name }}</li>
          <li @click="exit">退出</li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Header',
  data() {
    return {
    }
  },
  props: ['name'],
  methods: {
    exit(){
      this.$emit('onDoExit')
    }
  }
}
</script>
<style lang="css" scoped>
.block-header{
  position: relative;
  min-width: 830px;
  width: 100%; min-height: 130px;
  /* background: rgb(43, 58, 77); */
  background: url("../assets/header.jpg") bottom;
  background-size: cover;
}
@media screen and (min-width: 600px){
  .block-logo{
    position: relative;
    float: left;
    width: 300px; height: 130px;
    margin: 0px 100px;
    z-index: 99;
  }
  .logo{
    position: relative;
    width: 130px;height: auto;
    z-index: 99;
  }
}
@media screen and (max-width: 1200px){
  .block-logo{
    position: relative;
    display: none;
  }
}
.mask{
  position: absolute;
  width: 100%;
  height: 100%;
  background: rgba(30, 40, 40, 0.3);
  z-index: 0;
}
.block-title{
  position: absolute;
  left: 0px;
  width: 100%;
  height: 130px;
}
.title{
  font-size: 37px;
  color: white;
  height: 130px; line-height: 130px;
  width: 100%;
  text-align: center;
}
.block-login{
  position: absolute;
  right: 1%;
  bottom: 3px;
  color: rgb(222, 222, 222);
}
.block-login li{
  float: left;
  padding: 2px 7px;
  cursor: default;
}
.block-login li:hover{
  color: white;
  background: rgba(10,10,10,0.3);
  border-radius: 3px;
}
</style>
