import{j as e,A as n,p as u,C as m,q as c}from"./index-BEM99ApS.js";const f=()=>{const t=[{id:1,salesPeriod:"2025-02-14 ~ 2025-02-20",restaurantName:"동양백반",sales:"900,000 원",depositStatus:"미입금",endDate:"2025-02-20",isCompleted:"입금처리"},{id:2,salesPeriod:"2025-02-14 ~ 2025-02-20",restaurantName:"멘지",sales:"2,100,000 원",depositStatus:"미입금",endDate:"2025-02-20",isCompleted:"입금처리"},{id:3,salesPeriod:"2025-02-14 ~ 2025-02-20",restaurantName:"미분당",sales:"1,200,000 원",depositStatus:"미입금",endDate:"2025-02-20",isCompleted:"입금처리"},{id:4,salesPeriod:"2025-02-14 ~ 2025-02-20",restaurantName:"장어죽집",sales:"1,000,000 원",depositStatus:"미입금",endDate:"2025-02-20",isCompleted:"입금처리"},{id:5,salesPeriod:"2025-02-14 ~ 2025-02-20",restaurantName:"맥도날드",sales:"1,500,000 원",depositStatus:"미입금",endDate:"2025-02-20",isCompleted:"입금처리"},{id:6,salesPeriod:"2025-02-14 ~ 2025-02-20",restaurantName:"시골집",sales:"2,000,000 원",depositStatus:"미입금",endDate:"2025-02-20",isCompleted:"입금처리"},{id:7,salesPeriod:"2025-02-14 ~ 2025-02-20",restaurantName:"빽다방",sales:"1,000,000 원",depositStatus:"미입금",endDate:"2025-02-20",isCompleted:"입금처리"}],l=10,i=Array.from({length:l},(s,a)=>({id:a+1,salesPeriod:"",restaurantName:"",sales:"",depositStatus:"",endDate:"",isCompleted:""})),r=t.length<l?[...t,...i.slice(t.length)]:t,d=[{headerName:"순번",field:"id",sortable:!0,filter:!0,width:100},{headerName:"매출기간",field:"salesPeriod",sortable:!0,filter:!0,width:250},{headerName:"식당명",field:"restaurantName",sortable:!0,filter:!0,width:200},{headerName:"매출액",field:"sales",sortable:!0,filter:!0,width:200},{headerName:"입금 상태",field:"depositStatus",sortable:!0,filter:!0,width:120},{headerName:"최종 결제일",field:"endDate",sortable:!0,filter:!0,width:150},{headerName:"비고",field:"isCompleted",sortable:!0,filter:!0,width:120,cellRenderer:s=>{const a=s.value==="입금처리"?"flex w-full h-[80%] justify-center items-center bg-blue text-white rounded-md":"";return e.jsx("div",{className:"flex w-full h-full items-center",children:e.jsx("button",{onClick:()=>o(s),className:a,children:s.value||""})})}}],o=s=>{console.log(`Button clicked for ${s.data.restaurantName}`)};return e.jsxs("div",{className:"relative flex flex-col w-full h-dvh bg-white",children:[e.jsx(n,{title:"정기정산"}),e.jsxs("div",{className:"flex flex-col w-full h-dvh overflow-x-hidden overflow-y-scroll scrollbar-hide",children:[e.jsx("div",{className:"flex flex-col w-[100%] h-[10%] px-10 pt-10 bg-white items-start pointer-events-none",children:"이번 주 정산 내역"}),e.jsx("div",{className:"flex ag-theme-alpine w-full h-full justify-start px-10",children:e.jsx(u,{columnDefs:d,rowData:r,pagination:!0,paginationPageSize:10,domLayout:"print",modules:[m],theme:"legacy"})})]})]})},p=()=>{const t=()=>{console.log("1")};return e.jsxs("div",{className:"flex",children:[e.jsx(c,{onMenuClick:t}),e.jsx(f,{})]})};export{p as default};
