import {MenuItem} from './menu-item';
import {Roles} from "./shared/models/roles";

const CanAccessHome = (roles: Roles) => {
  return true;
};



export const MENU_ITEMS: MenuItem[] =
  [
    {
      title: 'COMPONENTS.HOME',
      key: 'COMPONENTS.HOME',
      icon: 'home',
      guards: [CanAccessHome],
      link: '/pages',
      home: true,
    },
    {
      title: 'COMPONENTS.CLIENT_MANAGEMENT',
      key: 'COMPONENTS.CLIENT_MANAGEMENT',
      icon: 'user-plus',
      guards: [],
      children: [
        {
          title: 'COMPONENTS.CLIENT_LIST',
          key: 'COMPONENTS.CLIENT_LIST',
          link: '/home/clients',
          hidden: false
        },
      ],
    }
  ];

