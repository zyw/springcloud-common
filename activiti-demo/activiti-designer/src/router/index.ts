import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Baics from '../views/BasicView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/study',
    name: 'Study',
    component: () => import('../views/BpmnStudyView.vue'),
    children: [
      {
        path: '',
        name: 'Baics',
        component: Baics
      },
      {
        path: 'provider',
        name: 'Provider',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/ProviderView.vue')
      },
      {
        path: 'panel',
        name: 'Panel',
        component: () => import('../views/PanelView.vue')
      },
      {
        path: 'axios',
        name: 'Axios',
        component: () => import('../views/AxiosView.vue')
      },
      {
        path: 'save',
        name: 'Save',
        component: () => import('../views/SaveView.vue')
      },
      {
        path: 'event',
        name: 'Event',
        component: () => import('../views/EventView.vue')
      }
    ]
  },
  {
    path: '/custom',
    name: 'Custom',
    component: () => import('../views/CustomBpmnView.vue'),
    children: [
      {
        path: '',
        name: 'Palette',
        component: () => import('../views/custom/PaletteView.vue')
      },
      {
        path: 'modeler',
        name: 'Modeler',
        component: () => import('../views/custom/ModelerView.vue')
      },
    ]
  },
  {
    path: '/renderer',
    name: 'Renderer',
    component: () => import('../views/CustomRendererView.vue'),
    children: [
      {
        path: '',
        name: 'Renderer',
        component: () => import('../views/custom/RendererView.vue')
      },
      {
        path: 'modeler',
        name: 'Modeler',
        component: () => import('../views/custom/ModelerView.vue')
      },
      {
        path: 'context-pad',
        name: 'ContextPad',
        component: () => import('../views/custom/ContextPadView.vue')
      },
    ]
  },
  {
    path: '/bpmn',
    name: "BPMN",
    component: () => import('../views/bpmn/BpmnView.vue'),
    children: [
      {
        path: '',
        name: 'Renderer',
        component: () => import('../views/bpmn/RendererView.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
