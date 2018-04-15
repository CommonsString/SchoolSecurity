<template>
  <div>
  <Modal v-model="ShowAlterStu" width="360" :closable="false" :mask-closable="false">
    <p slot="header" style="color:#f60;text-align:center">
      <span>修改学生信息</span>
    </p>
    <!-- msg:{{this.stuMsg}}<br>
    form: {{this.stuForm}} -->
    <div style="text-align:center">
      <Form ref="stuMsg" :label-width="80">
        <Form-item label="班级" prop="clasz">
          <Input type="text" v-model="clasz"></Input>
        </Form-item>
        <Form-item label="姓名" prop="name">
          <Input type="text" v-model="name"></Input>
        </Form-item>
        <Form-item label="手机号码" prop="tel">
          <Input type="text" v-model="tel"></Input>
        </Form-item>
        <Form-item label="学号" prop="stuNumber">
          <Input type="text" disabled v-model="stuNumber"></Input>
        </Form-item>
        <Form-item label="学院" prop="college">
          <Input type="text" v-model="college"></Input>
        </Form-item>
      </Form>
    </div>
    <div slot="footer">
      <Button type="primary"  @click="Cancel">取消</Button>
     <Button type="primary"  @click="Comfirm('stuMsg')">确认修改</Button>
   </div>
   </Modal>
  </div>
</template>
<script>
import ROUTE from '@/assets/ROUTE'
export default {
  props: {
    ShowAlterStu: false
  },

  data() {
    return {
       stuMsg: {
        clasz: '',
        name: '',
        tel: '',
        stuNumber: '',
        college: ''
      },
       clasz: '',
        name: '',
        tel: '',
        stuNumber: '',
        college: ''
    }
  },
  methods: {
    insert(){
      this.clasz=this.stuMsg.clasz
      this.name=this.stuMsg.name
      this.tel=this.stuMsg.tel
      this.stuNumber=this.stuMsg.stuNumber
      this.college=this.stuMsg.college
    },
    alterDatas(stuMsg) {
      this.$http.post(
        ROUTE.toReplace+'/infoSearchAndFix/alterStuInfo',//请求路径
        {
          clasz: this.clasz,
          name: this.name,
          tel: this.tel,
          stuNumber: this.stuNumber,
          college: this.college
        },
        { emulateJSON: true }
      )
        .then(function (res) {
          if(res.data.state==true){
            this.$Message.success(res.data.message)
            this.data6 = res.data.studentList//进行的处理 
          }
          else
          this.$Message.error(res.data.message)
        })
        .catch(function (err) {
          this.$Message.error('系统连接失败!' + err)
        })
    },
    Comfirm() {
      this.ShowAlterStu = false;
      this.alterDatas();
      this.$emit('my-event', false);
    },
    Cancel(){
      this.ShowAlterStu=false;
      this.$emit('my-event',false);
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>

