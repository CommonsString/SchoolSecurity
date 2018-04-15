import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
    mode: 'history',
    routes: [{
            path: '/',
            redirect: '/workSpace'
        },
        { //安全工作台
            path: '/workSpace',
            name: 'workSpace',
            meta: {
                level: 3
            },
            // component: WorkSpace
            component: resolve => require(['@/pages/info/workSpace'], resolve)
        },
        { //网格信息
            path: '/grid',
            name: 'grid',
            meta: {
                level: 3
            },
            // component: Grid
            component: resolve => require(['@/pages/info/grid'], resolve)
        },
        { //下属管理
            path: '/subManagement',
            name: 'subManagement',
            meta: {
                level: 2
            },
            // component: SubManagement
            component: resolve => require(['@/pages/info/subManagement'], resolve)
        },
        { //用户管理
            path: '/userManagement',
            name: 'userManagement',
            meta: {
                level: 1
            },
            // component: UserManagement
            component: resolve => require(['@/pages/info/userManagement'], resolve)
        },
        { //学生信息
            path: '/stuInfo',
            name: 'stuInfo',
            meta: {
                level: 2
            },
            // component: StuInfo
            component: resolve => require(['@/pages/info/stuInfo'], resolve)
        },
        ////////////////////////////////////////////////////////////////
        {
            path: '/eventHistory',
            name: 'eventHistory',
            meta: {
                level: 1
            },
            // component: EventHistory
            component: resolve => require(['@/pages/work/eventHistory'], resolve)
        },
        {
            path: '/eventIncre',
            name: 'eventIncre',
            meta: {
                level: 2
            },
            // component: EventIncre
            component: resolve => require(['@/pages/work/eventIncre'], resolve)
        },
        {
            path: '/eventList',
            name: 'eventList',
            meta: {
                level: 2
            },
            // component: EventList
            component: resolve => require(['@/pages/work/eventList'], resolve)
        },
        ////////////////////////////////////////////////////////////////
        {
            path: '/noticeListDep',
            name: 'noticeListDep',
            meta: {
                level: 2
            },
            // component: NoticeListDep
            component: resolve => require(['@/pages/notice/noticeListDep'], resolve)
        },
        {
            path: '/noticeListMy',
            name: 'noticeListMy',
            meta: {
                level: 2
            },
            // component: NoticeListMy
            component: resolve => require(['@/pages/notice/noticeListMy'], resolve)
        },
        {
            path: '/noticeUpload',
            name: 'noticeUpload',
            meta: {
                level: 1
            },
            // component: NoticeUpload
            component: resolve => require(['@/pages/notice/noticeUpload'], resolve)
        },
        {
            path: '/noticeGrid',
            name: 'noticeGrid',
            meta: {
                level: 1
            },
            // component: NoticeGrid
            component: resolve => require(['@/pages/notice/noticeGrid'], resolve)
        },
        {
            path: '/noticeListGrid',
            name: 'noticeListGrid',
            meta: {
                level: 1
            },
            // component: NoticeListGrid
            component: resolve => require(['@/pages/notice/noticeListGrid'], resolve)
        }
    ]
})

function getCookie(key) {
    var arr = document.cookie.split('; ');
    for (var i = 0; i < arr.length; i++) {
        var temp = arr[i].split('=');
        if (temp[0] == key) {
            return temp[1];
        }
    }
    return '';
}
router.beforeEach((to, from, next) => {
    //获取用户的等级信息
    try {
        let userLevel = JSON.parse(getCookie('userInfo')).level
            //当用户权限模高于块限制等级时进行跳转(数值上越低权限越高)
        if (to.matched.some(res => userLevel <= res.meta.level)) {
            // console.log('to')
            // console.log(to)
            next()
        }
        //否则提示并跳转
        else {
            alert('无权限!')
            next({ path: '/' })
        }
    } catch (err) {
        this.$Message.error('错误')
    }
})
export default router
