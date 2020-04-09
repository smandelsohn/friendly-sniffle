

insert into printRequest(id, printType, provider, uuid) values (1, '1', '2', 'to accuzip 1');
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (2, '1', '2', 'to accuzip 2', '', null);
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (3, '1', '2', 'to accuzip 3', null, '');
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (4, '1', '2', 'to accuzip 4', null, null);
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (5, '1', '2', 'to accuzip 5', '', '');
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (6, '1', '2', 'to accuzip 6', 'BAD ST', '');
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (7, '1', '2', 'to accuzip 7', 'PROC', 'guid');
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (8, '1', '2', 'to accuzip 8', 'NEW', null );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (9, '1', '2', 'to accuzip 9', 'CSVUP', 'accu-guid-req-job' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (10, '1', '2', 'to accuzip 10', 'NEW', 'new-123-123-123' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (11, '1', '2', 'to accuzip 11', 'REQ', 'ASDF-QWER-ZXVV-1234' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (12, '1', '2', 'to accuzip 12', 'REQ', '1212-QWER-ZXVV-1234' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (13, '1', '2', 'to accuzip 12', 'REQ', '1111-3333-ZXVV-1234' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (14, '1', '2', 'Will not pickup because order not completed', 'DONE', '4444-SDFG-ZXVV-1234' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (15, '1', '2', 'For this print request order completed', 'DONE', '5555-EDDF-DEAD-BEEF' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (16, '1', '2', 'For this print request order completed', 'DONE', '5555-EDDF-DEAD-BEEF' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (20, '1', '2', 'Clean up verified addresses test', 'ANY', 'CAFE-BABE-CAFE-BABE' );
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (1012, '1', '2', 'Bad num', 'REQ', 'BAD-NUM-ZXVV-1234' );


insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (1000, 6, 'Just for id to addresses flow test', 'address2', 'addressName', 'tttype', 'have no idea', 'San-Diego', 'CR', 'John', 'Dou', 'CA', '60123', 0);
insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (1001, 6, 'Just for id to addresses flow test Second', 'a2', 'addressName', 'tttype', 'have no idea', 'San-Diego', 'CR', 'John', 'Dou', 'CA', '60123', 0);


insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (1002, 8, 'addr 8.1.1', 'addr 8.1.2', 'addressName 8.1', 'tttype', 'have no idea', 'San-Diego', 'CR', 'John 8.1', 'Da 8.1', 'CA', '12345', 0);
insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (1003, 8, 'addr 8.2.1', 'addr 8.2.2', 'addressName 8.2', 'tttype', 'have no idea', 'Lost Angeles', 'BMW', 'Ivan 8.2', 'Puzan 8.2', 'UT', '55443', 0);
insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (1004, 8, 'addr 8.3.1', 'addr 8.3.2', 'addressName 8.3', 'tttype', 'have no idea', 'Sacramento', 'Apple', 'Vin 8.3', 'Dizel 8.3', 'NC', '99887', 0);
insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (2000, 8, '', '', '', '', '', '', '', 'Empty', '', '', '', 0);


insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (1005, 10, 'addr 10.1.1', 'addr 10.1.2', 'addressName 9.1', 'tttype', 'have no idea', 'Sacramento', 'Apple', 'Fransis', 'Drake', 'SC', '55447', 0);

insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (1006, 10, 'addr 10.2.1', 'addr 10.2.2', 'addressName 9.2', 'tttype', 'have no idea', 'Sacramento', 'Sony', 'Ivona', 'Obrams', 'WA', '99881', 0);

insert into printAddress (id, printRequestId, address1, address2, addressName, addressType, addressee, city, company, firstName, lastName, state, zip, certified)
    values (1020, 20, 'addr line 1020-1', 'addr line 1020-2', 'office', 'tttype', 'have no idea', 'Sacramento', 'Sony', 'Sweet', 'Monika', 'WA', '91020', 0);

insert into cart_id ( print_request_id, complete, userid) values ( 14, false, 123);
insert into cart_id ( print_request_id, complete, sAddress, userid) values ( 2, true, 'Will not pickup, because PR not in DONE state', 234);
insert into cart_id ( print_request_id, complete, sAddress, userid) values ( 1232, true, 'Will not pickup, because PR does not exists', 345);
insert into cart_id ( print_request_id, complete, sAddress, userid) values ( 15, true, 'Will be picked up and need to check, that two verified records are present in new table, and pr status is DONE', 34566);
insert into cart_id ( print_request_id, complete, sAddress, userid) values ( 16, true, 'Will be picked up and need to check, that two verified records are present in new table, and pr status is DONE', 34566);

-- AccuConnect data --
insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (100, '1', '2', 'ToAccuConnect', 'DONE', '5555-5555-5555-5551' );

insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (200, '1', '2', 'ToAccuConnect', 'DONE', '5555-5555-5555-5552' );

insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (300, '1', '2', 'ToAccuConnect', 'DONE', '5555-5555-5555-5553' );

insert into printRequest(id, printType, provider, uuid, acc_status, acc_uuid) values (400, '1', '2', 'ToAccuConnect', 'DONE', '5555-5555-5555-5554' );


insert into item (id, code, name, isPrinted) values (1135,'HVA-1072','16-Page Booklet -No Finish',1);
insert into item (id, code, name, isPrinted) values (1136,'HVA-1073','16-Page Booklet -Gloss',1);
insert into item (id, code, name, isPrinted) values (1137,'HVA-1074','16-Page Booklet -Matte',1);
insert into item (id, code, name, isPrinted) values (1141,'HVA-1078','32-Page Booklet -No Finish',1);
insert into item (id, code, name, isPrinted) values (1142,'HVA-1079','32-Page Booklet -Gloss',1);
insert into item (id, code, name, isPrinted) values (1143,'HVA-1080','32-Page Booklet -Matte',1);
insert into item (id, code, name, isPrinted) values (1096,'HVA-1033','1 Sided Flyer -No Finish',1);
insert into item (id, code, name, isPrinted) values (1097,'HVA-1034','1 Sided Flyer -Gloss',1);
insert into item (id, code, name, isPrinted) values (1098,'HVA-1035','1 Sided Flyer -Matte',1);
insert into item (id, code, name, isPrinted) values (1149,'HVA-2000','Standard Postage for 1 Sided Flyer',1);
insert into item (id, code, name, isPrinted) values (1150,'HVA-2001','First Class Postage for 1 Sided Flyer',1);
insert into item (id, code, name, isPrinted) values (1099,'HVA-1036','2 Sided Flyer -No Finish',1);
insert into item (id, code, name, isPrinted) values (1100,'HVA-1037','2 Sided Flyer -Gloss',1);
insert into item (id, code, name, isPrinted) values (1101,'HVA-1038','2 Sided Flyer -Matte',1);
insert into item (id, code, name, isPrinted) values (1151,'HVA-2002','Standard Postage for 2 Sided Flyer',1);
insert into item (id, code, name, isPrinted) values (1152,'HVA-2003','First Class Postage for 2 Sided Flyer',1);
insert into item (id, code, name, isPrinted) values (1102,'HVA-1039','4 Page Brochure -No Finish',1);
insert into item (id, code, name, isPrinted) values (1103,'HVA-1040','4 Page Brochure -Gloss',1);
insert into item (id, code, name, isPrinted) values (1104,'HVA-1041','4 Page Brochure -Matte',1);
insert into item (id, code, name, isPrinted) values (1105,'HVA-1057','4 Page Premium Brochure -No Finish',1);
insert into item (id, code, name, isPrinted) values (1106,'HVA-1058','4 Page Premium Brochure -Gloss',1);
insert into item (id, code, name, isPrinted) values (1107,'HVA-1059','4 Page Premium Brochure -Matte',1);
insert into item (id, code, name, isPrinted) values (1108,'HVA-1042','6 Page Brochure -No Finish',1);
insert into item (id, code, name, isPrinted) values (1109,'HVA-1043','6 Page Brochure -Gloss',1);
insert into item (id, code, name, isPrinted) values (1110,'HVA-1044','6 Page Brochure -Matte',1);
insert into item (id, code, name, isPrinted) values (1146,'HVA-1091','8 Page Brochure -No Finish',1);
insert into item (id, code, name, isPrinted) values (1147,'HVA-1092','8 Page Brochure -Gloss',1);
insert into item (id, code, name, isPrinted) values (1148,'HVA-1093','8 Page Brochure -Matte',1);
insert into item (id, code, name, isPrinted) values (1111,'HVA-1060','8 Page Premium Brochure -No Finish',1);
insert into item (id, code, name, isPrinted) values (1112,'HVA-1061','8 Page Premium Brochure -Gloss',1);
insert into item (id, code, name, isPrinted) values (1113,'HVA-1062','8 Page Premium Brochure -Matte',1);
insert into item (id, code, name, isPrinted) values (1153,'HVA-1101','12 Page Brochure -No Finish',1);
insert into item (id, code, name, isPrinted) values (1154,'HVA-1102','12 Page Brochure -Gloss',1);
insert into item (id, code, name, isPrinted) values (1155,'HVA-1103','12 Page Brochure -Matte',1);
insert into item (id, code, name, isPrinted) values (1114,'HVA-1063','12 Page Premium Brochure -No Finish',1);
insert into item (id, code, name, isPrinted) values (1115,'HVA-1064','12 Page Premium Brochure -Gloss',1);
insert into item (id, code, name, isPrinted) values (1116,'HVA-1065','12 Page Premium Brochure -Matte',1);
insert into item (id, code, name, isPrinted) values (1117,'HVA-1045','6 x 11 Mail Card -No Finish',1);
insert into item (id, code, name, isPrinted) values (1118,'HVA-1046','6 x 11 Mail Card -Gloss',1);
insert into item (id, code, name, isPrinted) values (1119,'HVA-1047','6 x 11 Mail Card -Matte',1);
insert into item (id, code, name, isPrinted) values (1156,'HVA-2018','Standard Postage for 6 x 11 Mail Card',1);
insert into item (id, code, name, isPrinted) values (1157,'HVA-2019','First Class Postage for 6 x 11 Mail Card',1);
insert into item (id, code, name, isPrinted) values (1120,'HVA-1048','6 x 9.75 Mail Card -No Finish',1);
insert into item (id, code, name, isPrinted) values (1121,'HVA-1049','6 x 9.75 Mail Card -Gloss',1);
insert into item (id, code, name, isPrinted) values (1122,'HVA-1050','6 x 9.75 Mail Card -Matte',1);
insert into item (id, code, name, isPrinted) values (1158,'HVA-2020','Standard Postage for 6 x 9.75 Mail Card',1);
insert into item (id, code, name, isPrinted) values (1159,'HVA-2021','First Class Postage for 6 x 9.75 Mail Card',1);
insert into item (id, code, name, isPrinted) values (1123,'HVA-1051','5.5 x 8.5 Mail Card -No Finish',1);
insert into item (id, code, name, isPrinted) values (1124,'HVA-1052','5.5 x 8.5 Mail Card -Gloss',1);
insert into item (id, code, name, isPrinted) values (1125,'HVA-1053','5.5 x 8.5 Mail Card -Matte',1);
insert into item (id, code, name, isPrinted) values (1160,'HVA-2022','Standard Postage for 5.5 x 8.5 Mail Card',1);
insert into item (id, code, name, isPrinted) values (1161,'HVA-2023','First Class Postage for 5.5 x 8.5 Mail Card',1);
insert into item (id, code, name, isPrinted) values (1126,'HVA-1054','4 x 6 Mail Card -No Finish',1);
insert into item (id, code, name, isPrinted) values (1127,'HVA-1055','4 x 6 Mail Card -Gloss',1);
insert into item (id, code, name, isPrinted) values (1128,'HVA-1056','4 x 6 Mail Card -Matte',1);
insert into item (id, code, name, isPrinted) values (1162,'HVA-2024','Standard Postage for 4 x 6 Mail Card',1);
insert into item (id, code, name, isPrinted) values (1163,'HVA-2025','First Class Postage for 4 x 6 Mail Card',1);
insert into item (id, code, name, isPrinted) values (1129,'HVA-1066','4-Page Mini Mailer -No Finish',1);
insert into item (id, code, name, isPrinted) values (1130,'HVA-1067','4-Page Mini Mailer -Gloss',1);
insert into item (id, code, name, isPrinted) values (1131,'HVA-1068','4-Page Mini Mailer -Matte',1);
insert into item (id, code, name, isPrinted) values (1164,'HVA-2026','Standard Postage for 4-Page Mini Mailer',1);
insert into item (id, code, name, isPrinted) values (1165,'HVA-2027','First Class Postage for 4-Page Mini Mailer',1);
insert into item (id, code, name, isPrinted) values (1132,'HVA-1069','6-Page Mini Mailer -No Finish',1);
insert into item (id, code, name, isPrinted) values (1133,'HVA-1070','6-Page Mini Mailer -Gloss',1);
insert into item (id, code, name, isPrinted) values (1134,'HVA-1071','6-Page Mini Mailer -Matte',1);
insert into item (id, code, name, isPrinted) values (1166,'HVA-2028','Standard Postage for 6-Page Mini Mailer',1);
insert into item (id, code, name, isPrinted) values (1167,'HVA-2029','First Class Postage for 6-Page Mini Mailer',1);
insert into item (id, code, name, isPrinted) values (1168,'HVA-2030','Standard Postage for 16-Page Booklet',1);
insert into item (id, code, name, isPrinted) values (1169,'HVA-2031','First Class Postage for 16-Page Booklet',1);
insert into item (id, code, name, isPrinted) values (1138,'HVA-1075','24-Page Booklet -No Finish',1);
insert into item (id, code, name, isPrinted) values (1139,'HVA-1076','24-Page Booklet -Gloss',1);
insert into item (id, code, name, isPrinted) values (1140,'HVA-1077','24-Page Booklet -Matte',1);
insert into item (id, code, name, isPrinted) values (1170,'HVA-2032','Standard Postage for 24-Page Booklet',1);
insert into item (id, code, name, isPrinted) values (1171,'HVA-2033','First Class Postage for 24-Page Booklet',1);
insert into item (id, code, name, isPrinted) values (1172,'HVA-2034','Standard Postage for 32-Page Booklet',1);
insert into item (id, code, name, isPrinted) values (1173,'HVA-2035','First Class Postage for 32-Page Booklet',1);


insert into cart_id ( id, print_request_id, complete, aZip, aAddress, userid, astate, acity, hvid) values (200, 100, 1, '27282', '123 abc street', 5768, 'CA', 'San-Diego', 5);

insert into cart_id ( id, print_request_id, complete, aZip, aAddress, userid, astate, acity, hvid) values (300, 200, 1, '27282', '123 abc street', 25846745, 'GA', 'Atlanta', 5);

insert into cart_id ( id, print_request_id, complete, aZip, aAddress, userid, hvid) values (400, 300, 1, '12345', '400-300 0 verified addresses street', 25846745, 5);

insert into cart_id ( id, print_request_id, complete, aZip, aAddress, userid, hvid) values (500, 400, 1, '23456', '500-400 0 pdfs ', 25846745, 5);

insert into cart (id,  cartid, selection, qty, chili_print_id) values (1134514, 200, 1124, 5, 23498);
insert into cart (id,  cartid, selection, qty, chili_print_id) values (1134515, 200, 1152, 5, 23498);

insert into cart (id,  cartid, selection, qty, chili_print_id) values (1134471, 300, 1099, 5, 23499);
insert into cart (id,  cartid, selection, qty, chili_print_id) values (1134472, 300, 1156, 5, 23499);

insert into cart (id,  cartid, selection, qty, chili_print_id) values (1134160, 400, 1101, 5, 23502);
insert into cart (id,  cartid, selection, qty, chili_print_id) values (1134161, 400, 1152, 5, 23502);

insert into cart (id,  cartid, selection, qty, chili_print_id) values (1134100, 500, 1128, 5, 23509);
insert into cart (id,  cartid, selection, qty, chili_print_id) values (1134102, 500, 1152, 5, 23509);


insert into chiliUserPrint (id, agentid,agentLayerTag) values (24785,27034,'1-Agent');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24807,27061,'1-Agent');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24809,27034,'1-Agent');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24811,27061,'1-Agent');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24813,27034,'1-Agent');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24816,27072,'1-AgentNoPhoto');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24819,27061,'1-AgentLogo');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24821,27061,'1-AgentLogo');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24822,27061,'1-AgentLogo');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24823,27061,'1-AgentLogo');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24826,27034,'1-AgentLogo');
insert into chiliUserPrint (id, agentid,agentLayerTag) values (24833,27070,'1-AgentNoPhoto');


insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80833,24785,'285507_24785-23461_1.pdf',1,3223);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80834,24785,'285507_24785-23461_2.pdf',2,3226);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID, selectedLayoutID) values (80872,24807,'285548_24807-23483_1.pdf',1,3231, 'AE8F2A62-294B-2995-68A0-3E3726CE3DAE');
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80873,24807,'285548_24807-23483_2.pdf',2,3230);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80876,24809,'285551_24809-23485_1.pdf',1,3298);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80877,24809,'285551_24809-23485_2.pdf',2,3301);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80879,24811,'285550_24811-23487_1.pdf',1,3231);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80880,24811,'285550_24811-23487_2.pdf',2,3230);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80882,24813,'285555_24813-23489_1.pdf',1,3298);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80883,24813,'285555_24813-23489_2.pdf',2,3301);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80888,24816,'285559_24816-23492_1.pdf',1,3223);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80889,24816,'285559_24816-23492_2.pdf',2,3226);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80894,24819,'285562_24819-23495_1.pdf',1,3231);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80895,24819,'285562_24819-23495_2.pdf',2,3230);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80898,24821,'285566_24821-23497_1.pdf',1,3239);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80899,24821,'285566_24821-23497_2.pdf',2,3232);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80900,24822,'285567_24822-23498_1.pdf',1,3239);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80901,24822,'285567_24822-23498_2.pdf',2,3232);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID, selectedLayoutID) values (80902,24823,'285568_24823-23499_1.pdf',1,3293, '970DE87F-ABD0-99BF-4F04-3E3F31D9B161');
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80903,24823,'285568_24823-23499_2.pdf',2,3292);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80908,24826,'285573_24826-23502_1.pdf',1,3291);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80909,24826,'285573_24826-23502_2.pdf',2,3290);


--null value to test error flow
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80922,24833,null,1,3291);
--insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80922,24833,'285587_24833-23509_1.pdf',1,3291);
insert into chiliUserPrintPage (id, upid, printPagePdf ,pageNumber, mtID) values (80923,24833,'285587_24833-23509_2.pdf',2,3290);


insert into chiliUserPrintProduction(id, upid, thumbURL) values(23461,24785,'http://homevisit-dev.s3.amazonaws.com/img/285507/pdf/24785_23461_thumb.png');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23483,24807,'http://homevisit-dev.s3.amazonaws.com/img/285548/pdf/24807_23483_thumb.png');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23485,24809,'');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23487,24811,'http://homevisit-dev.s3.amazonaws.com/img/285550/pdf/24811_23487_thumb.png');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23489,24813,'');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23492,24816,'');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23495,24819,'http://homevisit-dev.s3.amazonaws.com/img/285562/pdf/24819_23495_thumb.png');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23497,24821,'http://homevisit-dev.s3.amazonaws.com/img/285566/pdf/24821_23497_thumb.png');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23498,24822,'http://homevisit-dev.s3.amazonaws.com/img/285567/pdf/24822_23498_thumb.png');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23499,24823,'http://homevisit-dev.s3.amazonaws.com/img/285568/pdf/24823_23499_thumb.png');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23502,24826,'http://homevisit-dev.s3.amazonaws.com/img/285573/pdf/24826_23502_thumb.png');
insert into chiliUserPrintProduction(id, upid, thumbURL) values(23509,24833,'http://homevisit-dev.s3.amazonaws.com/img/285587/pdf/24833_23509_thumb.png');





insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23734,3223,'[Default]', '6D58E045-7EC0-57BC-10C1-2AACD6ADA078',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23735,3223,'B','8F8EEC63-4678-F80D-B147-2AAE43BCD069',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23736,3223,'C','049C7E26-198C-0F72-4A9E-2AAE49F82F28',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23737,3223,'D','A8EC38A0-DED8-1CE3-08B4-2AAE4F41DEBF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23738,3223,'E','EA233A98-252F-1964-A194-2AAE55189FE7',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23739,3223,'F','BA42551B-2DC4-12D1-97FE-2AAE5CDF45DB',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23740,3223,'G','055A4418-82DF-6B56-3303-2AAE63F45BD6',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23741,3223,'H','4E366FDA-4271-8AA9-F783-2AAE6AB1005E',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23742,3223,'I','5A47E386-FC43-0783-61D6-2AAE6FA3CF92',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23771,3226,'[Default]', 'DC3B9A0A-3FD9-326E-C0C7-2E91996A835F',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23772,3226,'B','EE6A65AE-24A3-1427-F6DA-2EB545A01012',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23773,3226,'C','D26778FB-C08A-2EB9-0295-2EB56CD6473A',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23774,3226,'D','5129358F-7DCC-807D-8BC7-2EB66A74DEB4',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23775,3226,'E','BC759553-F8FD-5009-FB3D-2EB59A75EBF1',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23776,3226,'F','6EA713E4-E19A-B97A-4158-2EB5BDC76A48',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23777,3226,'G','0492D780-8BDE-EBCE-88E0-2EB5D658C276',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23778,3226,'H','007FD427-E9BE-9DAB-7B34-2EB5EDC0FEA0',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23779,3226,'I','C6FE5A40-7287-327B-0CAD-2EB60436A6EB',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23780,3226,'J','90A977E7-77CC-2237-95AF-2EB61E46728E',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23781,3226,'K','0D3F9B96-781E-A190-F404-2EB63693B7D7',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23782,3226,'L','D671A320-69C9-F2F3-450A-2EB6510458DF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23783,3226,'M','149C9970-6780-AAF4-EF45-2EB80FACAC67',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23784,3226,'N','D020539B-DED1-573D-85FD-2EB82A2B9B0C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23785,3226,'O','3B7465CD-0E02-590B-0B05-2EB84486D665',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23869,3230,'[Default]', '36DEA03B-05B2-5400-4ED5-4422860FE757',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23870,3230,'B','E4B77D3C-3805-2FE6-3E77-443371EBDF72',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23871,3230,'C','812CBF12-3835-C25A-DBBD-44337B477167',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23872,3230,'D','3D997398-9DA5-9ADA-1255-4433835EF6EB',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23873,3230,'E','1D8F4C7A-8994-21B1-3809-44338B35CA94',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23874,3230,'F','8A4410E9-F23D-ED38-594A-443393443F6E',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23875,3230,'G','8AA4F549-7126-D5C4-A49B-44339B636DA4',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23876,3230,'H','FCBA6A5B-D79A-CC3D-119F-4433A302C057',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23877,3230,'I','67BF517C-64C4-7908-C10D-4433AA922894',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23878,3230,'J','EBF51AE8-8BB6-388D-A02D-4433B3DCEAE8',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23879,3230,'K','2360528D-A4B2-D30F-42AF-4433BC2465CD',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23904,3232,'[Default]','79D14D16-8AF5-722D-C39F-3EB42CE75E46',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23905,3232,'B','8B1E4BDD-DA34-2E78-10A2-3F02A27008B0',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23906,3232,'C','A9D4EEEE-B174-42A1-51F3-3F02B7B51803',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23907,3232,'D','C0C92243-DE56-6E93-68EB-3F037D13A616',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23908,3232,'E','891DEF39-D264-A7F8-2576-3F038FE41C3D',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23909,3232,'F','F064D408-B2C6-D3ED-E78F-3F03A3992F44',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23910,3232,'G','A4235FE3-B669-11A8-9AE7-3F03B6850D28',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23911,3232,'H','8AB9A31E-05E9-D1FA-1BE7-3F03CDB8338C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23912,3232,'I','821A3CA7-0C09-F36C-9A74-3F03E1F4FBDF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23913,3232,'J','A0B48136-1B0C-D3C0-8F63-3F03F56BF4EF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23914,3232,'K','0E88058C-BB2B-BEAD-A07D-3F02CDFEABBD',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23915,3232,'L','9D847B21-2D63-56D1-F8D0-3F02E22E08D0',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23916,3232,'M','172DE4B1-DE4B-B1B2-E8DF-3F02FEFD232C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23917,3232,'N','DAD80531-0BE5-80FD-FB12-3F031C870570',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23918,3232,'O','5A926D7F-09AB-EBB3-CF22-3F0332D97227',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23919,3232,'P','49555234-A753-BF89-B1B7-3F0345C4A7DC',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23920,3232,'Q','E7107FFF-14E1-69C0-AFA4-3F036C3492FF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (23921,3232,'R','311F9C84-0F91-2145-8276-3F03589F0615',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24819,3290,'[Default]','36DEA03B-05B2-5400-4ED5-4422860FE757',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24820,3290,'B','E4B77D3C-3805-2FE6-3E77-443371EBDF72',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24821,3290,'C','812CBF12-3835-C25A-DBBD-44337B477167',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24822,3290,'D','3D997398-9DA5-9ADA-1255-4433835EF6EB',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24823,3290,'E','1D8F4C7A-8994-21B1-3809-44338B35CA94',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24824,3290,'F','8A4410E9-F23D-ED38-594A-443393443F6E',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24825,3290,'G','8AA4F549-7126-D5C4-A49B-44339B636DA4',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24826,3290,'H','FCBA6A5B-D79A-CC3D-119F-4433A302C057',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24827,3290,'I','67BF517C-64C4-7908-C10D-4433AA922894',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24828,3290,'J','EBF51AE8-8BB6-388D-A02D-4433B3DCEAE8',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24829,3290,'K','2360528D-A4B2-D30F-42AF-4433BC2465CD',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24842,3292,'[Default]','79D14D16-8AF5-722D-C39F-3EB42CE75E46',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24843,3292,'B','8B1E4BDD-DA34-2E78-10A2-3F02A27008B0',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24844,3292,'C','A9D4EEEE-B174-42A1-51F3-3F02B7B51803',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24845,3292,'D','C0C92243-DE56-6E93-68EB-3F037D13A616',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24846,3292,'E','891DEF39-D264-A7F8-2576-3F038FE41C3D',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24847,3292,'F','F064D408-B2C6-D3ED-E78F-3F03A3992F44',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24848,3292,'G','A4235FE3-B669-11A8-9AE7-3F03B6850D28',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24849,3292,'H','8AB9A31E-05E9-D1FA-1BE7-3F03CDB8338C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24850,3292,'I','821A3CA7-0C09-F36C-9A74-3F03E1F4FBDF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24851,3292,'J','A0B48136-1B0C-D3C0-8F63-3F03F56BF4EF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24852,3292,'K','0E88058C-BB2B-BEAD-A07D-3F02CDFEABBD',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24853,3292,'L','9D847B21-2D63-56D1-F8D0-3F02E22E08D0',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24854,3292,'M','172DE4B1-DE4B-B1B2-E8DF-3F02FEFD232C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24855,3292,'N','DAD80531-0BE5-80FD-FB12-3F031C870570',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24856,3292,'O','5A926D7F-09AB-EBB3-CF22-3F0332D97227',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24857,3292,'P','49555234-A753-BF89-B1B7-3F0345C4A7DC',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24858,3292,'Q','E7107FFF-14E1-69C0-AFA4-3F036C3492FF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24859,3292,'R','311F9C84-0F91-2145-8276-3F03589F0615',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24932,3298,'[Default]','6D58E045-7EC0-57BC-10C1-2AACD6ADA078',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24933,3298,'B','8F8EEC63-4678-F80D-B147-2AAE43BCD069',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24934,3298,'C','049C7E26-198C-0F72-4A9E-2AAE49F82F28',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24935,3298,'D','A8EC38A0-DED8-1CE3-08B4-2AAE4F41DEBF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24936,3298,'E','EA233A98-252F-1964-A194-2AAE55189FE7',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24937,3298,'F','BA42551B-2DC4-12D1-97FE-2AAE5CDF45DB',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24938,3298,'G','055A4418-82DF-6B56-3303-2AAE63F45BD6',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24939,3298,'H','4E366FDA-4271-8AA9-F783-2AAE6AB1005E',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24940,3298,'I','5A47E386-FC43-0783-61D6-2AAE6FA3CF92',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24969,3301,'[Default]','DC3B9A0A-3FD9-326E-C0C7-2E91996A835F',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24970,3301,'B','EE6A65AE-24A3-1427-F6DA-2EB545A01012',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24971,3301,'C','D26778FB-C08A-2EB9-0295-2EB56CD6473A',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24972,3301,'D','5129358F-7DCC-807D-8BC7-2EB66A74DEB4',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24973,3301,'E','BC759553-F8FD-5009-FB3D-2EB59A75EBF1',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24974,3301,'F','6EA713E4-E19A-B97A-4158-2EB5BDC76A48',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24975,3301,'G','0492D780-8BDE-EBCE-88E0-2EB5D658C276',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24976,3301,'H','007FD427-E9BE-9DAB-7B34-2EB5EDC0FEA0',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24977,3301,'I','C6FE5A40-7287-327B-0CAD-2EB60436A6EB',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24978,3301,'J','90A977E7-77CC-2237-95AF-2EB61E46728E',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24979,3301,'K','0D3F9B96-781E-A190-F404-2EB63693B7D7',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24980,3301,'L','D671A320-69C9-F2F3-450A-2EB6510458DF',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24981,3301,'M','149C9970-6780-AAF4-EF45-2EB80FACAC67',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24982,3301,'N','D020539B-DED1-573D-85FD-2EB82A2B9B0C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (24983,3301,'O','3B7465CD-0E02-590B-0B05-2EB84486D665',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27187,3231,'[Default]','AE8F2A62-294B-2995-68A0-3E3726CE3DAE',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27188,3231,'B','8188B60F-6781-6399-82C7-3E3859D89431',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27189,3231,'C','572984D4-4CBC-C28E-CDAB-3E3860628433',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27190,3231,'D','7530D008-8696-886E-63E1-3E389401E667',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27191,3231,'E','DA48F0AB-06CB-3709-32F4-3E3865AC4DDD',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27192,3231,'F','866C5446-AE9F-B5B9-81B2-3E386B7BB572',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27193,3231,'G','568A2959-2A71-4A8D-690C-3E387038F8FA',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27194,3231,'H','08C23732-4A6C-AD57-395A-3E3876EFA426',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27195,3231,'I','13C8F060-930E-D102-EC0F-3E387CE49C50',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27196,3231,'J','5905A5CD-EBA5-E13B-EE64-3E3883145068',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27197,3231,'K','0AE60E5D-E605-3691-FC18-3E388864912C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27198,3231,'L','E10B2B91-5638-8BEF-2514-3E388EE9F46E',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27199,3231,'M','01FA3EEF-8D89-0625-F876-EC1E9F2B822A',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27200,3231,'N','90ADF7BE-13FE-3B54-B856-EC2269CFA99C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27201,3231,'O','00595A77-A111-2FF0-DC64-EC2429B8716F',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27202,3231,'P','B8524F88-3278-426B-4D0E-EC268C39ADD6',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27203,3291,'[Default]','AE8F2A62-294B-2995-68A0-3E3726CE3DAE',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27204,3291,'B','8188B60F-6781-6399-82C7-3E3859D89431',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27205,3291,'C','572984D4-4CBC-C28E-CDAB-3E3860628433',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27206,3291,'D','7530D008-8696-886E-63E1-3E389401E667',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27207,3291,'E','DA48F0AB-06CB-3709-32F4-3E3865AC4DDD',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27208,3291,'F','866C5446-AE9F-B5B9-81B2-3E386B7BB572',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27209,3291,'G','568A2959-2A71-4A8D-690C-3E387038F8FA',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27210,3291,'H','08C23732-4A6C-AD57-395A-3E3876EFA426',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27211,3291,'I','13C8F060-930E-D102-EC0F-3E387CE49C50',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27212,3291,'J','5905A5CD-EBA5-E13B-EE64-3E3883145068',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27213,3291,'K','0AE60E5D-E605-3691-FC18-3E388864912C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27214,3291,'L','E10B2B91-5638-8BEF-2514-3E388EE9F46E',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27215,3291,'M','C0A877AA-BA18-F26A-AB97-EC29FF9DB06B',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27216,3291,'N','3C4A91D2-62C7-41AF-4E39-EC2B80381939',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27217,3291,'O','5E09C07A-BF0A-0570-0FDC-EC2D2331AFF9',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27218,3291,'P','F345CE8D-D1CA-1AF6-2167-EC2F491904A2',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27315,3239,'[Default]','42677489-8959-CE1C-B497-3E3505540FA2',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27316,3239,'B','CEBF3DB9-C360-4EAB-36BF-3E3EEC48E0C4',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27317,3239,'C','0C5131C4-2D3A-E894-68A0-3E3EF2C0F696',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27318,3239,'D','1DB2B410-D6AA-6F4F-FABC-3E3F265F710C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27319,3239,'E','360E9DF1-5FE9-1AF0-9447-3E3F2C7C3340',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27320,3239,'F','970DE87F-ABD0-99BF-4F04-3E3F31D9B161',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27321,3239,'G','E843B440-B3D9-D5ED-0EDF-3E3EFD907302',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27322,3239,'H','932920B5-2BBF-FBF3-3FF5-3E3F04535C3B',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27323,3239,'I','DF6E54E1-75C5-44A9-B611-3E3F0A988B04',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27324,3239,'J','7BFED795-9A1B-C199-496C-3E3F0FBB4BD4',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27325,3239,'K','192FB815-0660-5DAC-0FF8-3E3F160324A7',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27326,3239,'L','BA80CE61-7042-4EB1-22AD-3E3F1B07474D',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27327,3239,'M','D60D343E-C1CE-CD10-2744-F0023ECA60C8',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27328,3239,'N','811D41D6-4C26-9B4A-87F5-F0041A082D03',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27329,3239,'O','45B3EE68-E723-6CE8-67C2-F005C6F01898',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27330,3239,'P','8644C872-2AA2-1939-A9D1-F00B68B08CF9',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27331,3293,'[Default]','42677489-8959-CE1C-B497-3E3505540FA2',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27332,3293,'B','CEBF3DB9-C360-4EAB-36BF-3E3EEC48E0C4',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27333,3293,'C','0C5131C4-2D3A-E894-68A0-3E3EF2C0F696',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27334,3293,'D','1DB2B410-D6AA-6F4F-FABC-3E3F265F710C',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27335,3293,'E','360E9DF1-5FE9-1AF0-9447-3E3F2C7C3340',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27336,3293,'F','970DE87F-ABD0-99BF-4F04-3E3F31D9B161',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27337,3293,'G','E843B440-B3D9-D5ED-0EDF-3E3EFD907302',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27338,3293,'H','932920B5-2BBF-FBF3-3FF5-3E3F04535C3B',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27339,3293,'I','DF6E54E1-75C5-44A9-B611-3E3F0A988B04',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27340,3293,'J','7BFED795-9A1B-C199-496C-3E3F0FBB4BD4',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27341,3293,'K','192FB815-0660-5DAC-0FF8-3E3F160324A7',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27342,3293,'L','BA80CE61-7042-4EB1-22AD-3E3F1B07474D',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27343,3293,'M','1119B5B0-53B8-1009-D805-F0436EE2A568',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27344,3293,'N','4226EE41-97D1-3BFC-507B-F04565CF6137',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27345,3293,'O','1315DA10-D4D7-49D0-A5DA-F047C6270412',0);
insert into chiliMasterTemplateAltLayout (id, mtID , name, itemID, postagePlacement) values (27346,3293,'P','5A24B9B6-5A2C-B4C9-D535-F04A332049BB',0);



insert into chiliDefPostagePlacement (id, postagePlacement, placement) values (1,0,'HP');
insert into chiliDefPostagePlacement (id, postagePlacement, placement) values (2,1,'LP');

insert into printAddressVerified (id, print_request_id, first_name, last_name, address, address2, city, state, zip)
    values (1000, 100, 'John 100.1', 'Da 100.1', 'Ice cream street 23', 'addr 100.1.2', 'San-Diego', 'CA', '12345');
insert into printAddressVerified (id, print_request_id, first_name, last_name, address, address2, city, state, zip)
    values (1001, 100, 'John 100.2', 'Da 100.2', 'Long sun ave, 45', 'addr 100.2.2', 'San-Diego', 'CA', '12345');
insert into printAddressVerified (id, print_request_id, first_name, last_name, address, address2, city, state, zip)
    values (1002, 100, 'John 100.3', 'Da 100.3', 'Drunk st, 33', 'addr 100.3.2', 'San-Diego', 'CA', '12345');

insert into printAddressVerified (id, print_request_id, first_name, last_name, address, address2, city, state, zip)
    values (2000, 200, 'John 200.1', 'Da 200.1', 'Pretty st, 33', 'addr 200.1.2', 'San-Diego', 'CA', '12345');

insert into printAddressVerified (id, print_request_id, first_name, last_name, address, address2, city, state, zip)
    values (3000, 400, 'Pearl 400.1', 'Jam 400.1', 'RnR st, 33', 'addr 200.1.2', 'Seatle', 'WA', '12345');


insert into chiliUserPrintProduction (id, print_request_id, printReadyURL ) values (2000, 100, 'http://www.domain.com/apc0.pdf');
insert into chiliUserPrintProduction (id, print_request_id, printReadyURL ) values (2001, 100, 'http://www.domain.com/apc1.pdf');
insert into chiliUserPrintProduction (id, print_request_id, printReadyURL ) values (2002, 100, 'http://www.domain.com/apc2.pdf');
insert into chiliUserPrintProduction (id, print_request_id, printReadyURL ) values (2003, 100, 'http://www.domain.com/apc3.pdf');
insert into chiliUserPrintProduction (id, print_request_id ) values (2004, 100);

insert into chiliUserPrintProduction (id, print_request_id, printReadyURL ) values (2100, 200, 'http://www.domain.com/apc200.pdf');

insert into chiliUserPrintProduction (id, print_request_id, printReadyURL ) values (2200, 250, 'http://www.domain.com/apc300.pdf');

insert into chiliUserPrintProduction (id, print_request_id, printReadyURL ) values (2300, 255, 'http://www.domain.com/apc400.pdf');




insert into agvproduct (id, product_type, mail_class) values (490,'PROPERTY_WEBSITE','');
insert into agvproduct (id, product_type, mail_class) values (491,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (492,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (493,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (494,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (495,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (496,'MEDIA','_EDIT_REQUEST');
insert into agvproduct (id, product_type, mail_class) values (497,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (498,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (499,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (500,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (501,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (502,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (503,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (504,'MEDIA','');
insert into agvproduct (id, product_type, mail_class) values (505,'PROPERTY_WEBSITE','');
insert into agvproduct (id, product_type, mail_class) values (506,'PRINT','');
insert into agvproduct (id, product_type, mail_class, sku) values (507,'PRINT_DIRECT_MAIL','Standard', 'HVA-2003');
insert into agvproduct (id, product_type, mail_class, sku) values (508,'PRINT_DIRECT_MAIL','First_Class', 'HVA-2018');
insert into agvproduct (id, product_type, mail_class) values (509,'PRINT','');
insert into agvproduct (id, product_type, mail_class, sku) values (510,'PRINT_DIRECT_MAIL','Standard', 'HVA-2023');
insert into agvproduct (id, product_type, mail_class, sku) values (511,'PRINT_DIRECT_MAIL','First_Class', 'HVA-2033');
insert into agvproduct (id, product_type, mail_class) values (512,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (513,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (514,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (515,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (516,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (517,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (518,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (519,'PRINT','');
insert into agvproduct (id, product_type, mail_class, sku) values (520,'PRINT_DIRECT_MAIL','Standard', 'HVA-2043');
insert into agvproduct (id, product_type, mail_class, sku) values (521,'PRINT_DIRECT_MAIL','First_Class', 'HVA-2053');
insert into agvproduct (id, product_type, mail_class) values (522,'PRINT','');
insert into agvproduct (id, product_type, mail_class, sku) values (523,'PRINT_DIRECT_MAIL','Standard', 'HVA-2063');
insert into agvproduct (id, product_type, mail_class, sku) values (524,'PRINT_DIRECT_MAIL','First_Class', 'HVA-2073');
insert into agvproduct (id, product_type, mail_class) values (525,'PRINT','');
insert into agvproduct (id, product_type, mail_class, sku) values (526,'PRINT_DIRECT_MAIL','Standard', 'HVA-2083');
insert into agvproduct (id, product_type, mail_class, sku) values (527,'PRINT_DIRECT_MAIL','First_Class', 'HVA-2093');
insert into agvproduct (id, product_type, mail_class) values (528,'PRINT','');
insert into agvproduct (id, product_type, mail_class, sku) values (529,'PRINT_DIRECT_MAIL','Standard', 'HVA-2103');
insert into agvproduct (id, product_type, mail_class, sku) values (530,'PRINT_DIRECT_MAIL','First_Class', 'HVA-2113');
insert into agvproduct (id, product_type, mail_class) values (531,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (532,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (533,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (534,'PRINT','');
insert into agvproduct (id, product_type, mail_class) values (535,'PRINT','');


insert into agvproductvariant (id, product_id,item_id, print_name ) values (598,491,1072,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (599,491,1073,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (600,491,1074,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (601,497,1078,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (602,497,1079,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (603,497,1080,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (604,497,1081,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (605,497,1082,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (606,499,1084,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (607,499,1085,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (608,499,1086,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (609,499,1087,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (610,499,1088,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (611,504,1094,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (612,504,1093,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (613,506,1096,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (614,506,1097,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (615,506,1098,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (616,509,1099,'8-5x11_Horizontal_None');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (617,509,1100,'8-5x11_Horizontal_UV');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (618,509,1101,'8-5x11_Horizontal_Matte');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (619,512,1102,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (620,512,1103,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (621,512,1104,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (622,514,1108,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (623,514,1109,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (624,514,1110,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (625,519,1117,'6x11_None');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (626,519,1118,'6x11_UV');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (627,519,1119,'6x11_Matte');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (628,522,1120,'6x9-75_None');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (629,522,1121,'6x9-75_UV');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (630,522,1122,'6x9-75_Matte');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (631,525,1123,'8-5x5-5_None');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (632,525,1124,'8-5x5-5_UV');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (633,525,1125,'8-5x5-5_Matte');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (634,528,1126,'4x6_None');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (635,528,1127,'4x6_UV');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (636,528,1128,'4x6_Matte');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (637,513,1105,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (638,513,1106,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (639,513,1107,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (640,516,1111,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (641,516,1112,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (642,516,1113,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (643,518,1114,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (644,518,1115,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (645,518,1116,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (646,531,1129,'10x6_BI-FOLD_None');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (647,531,1130,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (648,531,1131,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (649,532,1132,'8-5x5-5_TRI-FOLD_None');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (650,532,1133,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (651,532,1134,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (652,533,1135,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (653,533,1136,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (654,533,1137,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (655,534,1138,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (656,534,1139,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (657,534,1140,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (658,535,1141,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (659,535,1142,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (660,535,1143,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (661,515,1146,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (662,515,1147,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (663,515,1148,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (664,517,1153,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (665,517,1154,'');
insert into agvproductvariant (id, product_id,item_id, print_name ) values (666,517,1155,'');











