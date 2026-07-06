# PetCare 最終版

## 更新內容
- 管理員後台總覽頁可查看資料。
- AdminTableUI 新增 CRUD：新增、編輯、刪除、重新整理。
- 管理員可管理：會員、寵物、健康紀錄、疫苗紀錄、體重紀錄、提醒事項、成長相簿、醫療文件。
- 一般使用者可新增、編輯、刪除自己寵物底下的健康紀錄、疫苗、體重、提醒、相簿與醫療文件。
- 緊急摘要新增「列印」功能，可直接呼叫 Java Swing print。

## 執行方式
1. 先在 MySQL Workbench 執行 `sql/petcare_complete_mysql_weight_chart.sql`
2. Eclipse 匯入 Maven Project
3. 執行 `controller.LoginUI`

## 測試帳號
- 管理員：admin / 1234
- 使用者：user01 / 1234
