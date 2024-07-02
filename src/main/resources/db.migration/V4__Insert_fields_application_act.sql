-- Application header
insert into schema_system.field (created_at, default_value,
                                 name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Общество с ограниченной ответственностью %NameOfCustomer%, в дальнейшем именуемое «Заказчик», в лице генерального директора %FullNameOfCustomerResponsiblePerson%, действующего на основании Устава, с одной стороны, и Индивидуальный предприниматель %NameOfExecutor%, именуемый в дальнейшем «Исполнитель», в лице %FullNameOfExecutorResponsiblePerson%, действующего на основании Свидетельства о государственной регистрации физического лица в качестве индивидуального предпринимателя (%RegCertificate%, %RegNumber%), с другой стороны, далее именуемые «Стороны», заключили настоящее Приложение о нижеследующем:',
'application_header', 'Information about the customer and contractor',
false, 'Application field', now(), null, 2);


-- Division of application 1
insert into schema_system.field (created_at, default_value,
                                 name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '1.ПРЕДМЕТ ПРИЛОЖЕНИЯ%n
1.1.Исполнитель обязуется по заданию Заказчика оказать услуги по %NameOfService%. Услуга включает в себя %DescriptionOfService%.%n
1.2.Заказчик обязуется принять и оплатить указанные Услуги в соответствии с условиями настоящего Приложения.%n
1.3.Для оказания услуг в рамках настоящего приложения, Исполнитель вправе привлекать третьих лиц, оставаясь ответственным за их действия.%n',
        'application_div_1', 'Content of division',
        false, 'Application field', now(), null, 2);


-- Division of application 2
insert into schema_system.field (created_at, default_value,
                                 name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '2.ПРАВА И ОБЯЗАННОСТИ СТОРОН%n
2.1.Исполнитель обязан%n
2.1.1.Принять от Заказчика посредством электронной связи всю необходимую информацию и документацию для оказания Услуг в соответствии с настоящим Приложением.%n
2.1.2.Оказать услуги в соответствии с действующей нормативной документацией и требованиями законодательства Российской Федерации.%n
2.1.3.Предоставлять по запросам Заказчика информацию о ходе оказания Услуг.%n
2.2.Заказчик обязан%n
2.2.1.Передать Исполнителю посредством электронной связи на электронный адрес всю необходимую информацию для оказания Услуг в соответствии с настоящим Приложением. В случае недостаточности информации и/или документации для надлежащего исполнения Услуг Исполнитель вправе запросить такую информацию и/или документацию у Заказчика, а Заказчик обязуется ее предоставить в течение 3 (трех) рабочих дней с момента получения запроса, направленного в том числе посредством электронной связи. В случае не предоставления Заказчиком информации и/или документации в указанный срок, Исполнитель имеет право приостановить исполнение Услуг на соответствующий период до момента ее предоставления.%n
2.2.2.Проводить согласование рабочих материалов не более чем в 2 (две) итерации. При необходимости большего количества итераций, Исполнитель имеет право пересмотреть срок и стоимость оказания Услуг.%n
2.2.3.Проводить согласование рабочих материалов в течение 2 (двух) рабочих дней.%n
2.2.4.Оплатить Услуги Исполнителя в соответствии с условиями настоящего Приложения.%n',
        'application_div_2', 'Content of division',
        false, 'Application field', now(), null, 2);


-- Division of application 3
insert into schema_system.field (created_at, default_value,
                                 name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '3.ПЕРИОД ОКАЗАНИЯ УСЛУГ%n
3.1.Общий период оказания Услуг в соответствии с настоящим Приложением составляет: 10 календарных дней с момента подписания настоящего Приложения.%n
3.2.Приложение имеет возможность пролонгации.%n
3.3.В случае несвоевременного согласования рабочих материалов и/или несвоевременного предоставления материалов Заказчиком Исполнитель имеет право пересмотреть срок и стоимость оказания Услуг.%n',
        'application_div_3', 'Content of division',
        false, 'Application field', now(), null, 2);


-- Division of application 4
insert into schema_system.field (created_at, default_value,
                                 name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '4.СТОИМОСТЬ УСЛУГ И ПОРЯДОК РАСЧЕТОВ%n
4.1.Стоимость услуг по настоящему Приложению составляет %CostOfServices%, НДС не облагается.%n
4.2.Оплата производится на основании счетов, выставляемых Исполнителем.%n',
        'application_div_4', 'Content of division',
        false, 'Application field', now(), null, 2);


-- Division of application 5
insert into schema_system.field (created_at, default_value,
                                 name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '5.ХАРАКТЕРИСТИКИ УСЛУГ%n
%DetailsOfServices%%n',
        'application_div_5', 'Content of division',
        false, 'Application field', now(), null, 2);


-- Division of application 6
insert into schema_system.field (created_at, default_value,
                                 name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '6.ЗАКЛЮЧИТЕЛЬНЫЕ ПОЛОЖЕНИЯ%n
6.1.Настоящее Приложение вступает в силу с даты его подписания и действует до полного исполнения Сторонами обязательств по Приложению.%n
6.2.Настоящее Приложение составлено на русском языке в двух экземплярах, имеющих одинаковую юридическую силу по одному экземпляру для каждой из Сторон.%n',
        'application_div_6', 'Content of division',
        false, 'Application field', now(), null, 2);


-- Application footer
insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Заказчик:%n
%NameOfCustomer%%n
ИНН/КПП: %Inn%/%Kpp%%n
Адрес: %Address%%n
Банковские реквизиты:%n
%NameOfBank%%n
БИК: %Bic%%n
Р/с: %CheckingAccount%%n
К/с: %CorrAccount%',
        'application_customer_details_col', 'Customer details',
        false, 'Application field', now(), null, 2);

insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Исполнитель:%n
%NameOfExecutor%%n
ИНН: %Inn%%n
ОГРНИП: %RegCertificate%%n
Адрес: %Address%%n
Банковские реквизиты:%n
%NameOfBank%%n
БИК: %Bic%%n
Р/с: %CheckingAccount%%n
К/с: %CorrAccount%',
        'application_executor_details_col', 'Executor details',
        false, 'Application field', now(), null, 2);

insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Генеральный директор%n
%n
______________ /%ShortNameOfCustomerResponsiblePerson%/%n
%n
МП',
        'application_customer_sign', 'Customer sign',
        false, 'Application field', now(), null, 2);

insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '%ShortNameOfExecutor%%n
%n
______________ /%ShortNameOfExecutorResponsiblePerson%/%n
%n
МП',
        'application_executor_sign', 'Executor sign',
        false, 'Application field', now(), null, 2);


-- Act information
insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Заказчик: %NameOfCustomer%, ИНН/КПП: %Inn%/%Kpp%, Адрес: %Address%, Банковские реквизиты: %NameOfBank%, БИК: %Bic%, Р/с: %CheckingAccount%, К/с: %CorrAccount%',
        'act_customer_details_row', 'Customer details',
        false, 'Act field', now(), null, 3);

insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Исполнитель: %NameOfExecutor%, ИНН: %Inn%, ОГРНИП: %RegCertificate%, Адрес: %Address%, Банковские реквизиты: %NameOfBank%, БИК: %Bic%, Р/с: %CheckingAccount%, К/с: %CorrAccount%',
        'act_executor_details_row', 'Executor details',
        false, 'Act field', now(), null, 3);

insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Генеральный директор%n
%n
______________ /%ShortNameOfCustomerResponsiblePerson%/%n
%n
МП',
        'act_customer_sign', 'Customer sign',
        false, 'Act field', now(), null, 3);

insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '%ShortNameOfExecutor%%n
%n
______________ /%ShortNameOfExecutorResponsiblePerson%/%n
%n
МП',
        'act_executor_sign', 'Executor sign',
        false, 'Act field', now(), null, 3);


-- Account information
insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Заказчик: %NameOfCustomer%, ИНН/КПП: %Inn%/%Kpp%, Адрес: %Address%, Банковские реквизиты: %NameOfBank%, БИК: %Bic%, Р/с: %CheckingAccount%, К/с: %CorrAccount%',
        'account_customer_details_row', 'Customer details',
        false, 'Account field', now(), null, 4);

insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Исполнитель: %NameOfExecutor%, ИНН: %Inn%, ОГРНИП: %RegCertificate%, Адрес: %Address%, Банковские реквизиты: %NameOfBank%, БИК: %Bic%, Р/с: %CheckingAccount%, К/с: %CorrAccount%',
        'account_executor_details_row', 'Executor details',
        false, 'Account field', now(), null, 4);


insert into schema_system.field (created_at, default_value, name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), '%ShortNameOfExecutor%%n
%n
______________ /%ShortNameOfExecutorResponsiblePerson%/%n
%n
МП',
        'account_executor_sign', 'Executor sign',
        false, 'Account field', now(), null, 4);


-- Division of act final
insert into schema_system.field (created_at, default_value,
                                 name, placeholder,
                                 removed, type, updated_at, document_id, template_id)
values (now(), 'Исполнитель выполнил все обязательства в полном объёме в срок с надлежащим качеством. Заказчик претензий к исполнителю не имеет.',
        'act_div_final', 'Content of division',
        false, 'Act field', now(), null, 3);


