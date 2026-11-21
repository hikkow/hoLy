package ru.holly.script

data class TutStep(val key: String, val title: String, val hint: String)

object TutorialData {
    val steps = listOf(
        TutStep("NINJUROK6", "Верх цены запроса", "Пипеткой отметь верхнюю границу поля цены"),
        TutStep("NINJUROK7", "Низ цены запроса", "Теперь нижняя граница того же поля"),
        TutStep("NINJUROK13", "Поле ввода цены", "Тапни прямо в поле, куда вводится 0.03"),
        TutStep("NINJUROKOK", "Кнопка «ОК»", "Кнопка подтверждения цены"),
        TutStep("NINJUROK10", "Заказать 1", "Первая кнопка «Заказать»"),
        TutStep("NINJUROK12", "Заказать 2", "Вторая кнопка «Заказать»"),
        TutStep("NINJUROK11", "Отменить", "Кнопка отмены запроса"),
        TutStep("NINJUROKFREE", "Крестик", "Крестик закрытия окна"),
        TutStep("NINJUROKBALANCE1", "Баланс: левый верх", "Уголок рамки баланса (левый верх)"),
        TutStep("NINJUROKBALANCE2", "Баланс: правый низ", "Уголок рамки баланса (правый низ)"),
        TutStep("NINJUROKSELL", "Продать", "Кнопка «Продать»"),
        TutStep("NINJUROKSELLBACK", "Назад с продажи", "Кнопка «Назад» из окна продажи"),
        TutStep("DARKLINE2", "Галочка «Только мои запросы»", "Отметь чекбокс фильтра"),
        TutStep("DARKLINETOP", "Крестик (осмотр)", "Крестик закрытия окна осмотра"),
        TutStep("DARKLINETOP1", "Назад с осмотра", "Стрелка «Назад» из осмотра"),
        TutStep("DARKLINE3", "Подтвердить", "Кнопка подтверждения покупки"),
        TutStep("DARKLINEBUY", "Купить первый лот", "Кнопка «Купить» у первого лота"),
        TutStep("DARKLINESTICKERTOP", "Наклейка1: левый верх", "Рамка 1-й наклейки — левый верх"),
        TutStep("DARKLINESTICKERBOTTOM", "Наклейка1: правый низ", "Рамка 1-й наклейки — правый низ"),
        TutStep("DARKLINESTICKERSECONDTOP", "Наклейка2: левый верх", "Рамка 2-й наклейки — левый верх"),
        TutStep("DARKLINEBALANCE1", "Баланс2: левый верх", "Уголок рамки баланса (Nakl)"),
        TutStep("DARKLINEBALANCE2", "Баланс2: правый низ", "Уголок рамки баланса (Nakl)")
    )
}
