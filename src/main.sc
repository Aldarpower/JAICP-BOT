require: prod.js
require: slotfilling/slotFilling.sc
  module = sys.zb-common

theme: /

    state: Start
        q!: $regex</start>
        a: Приветствую, я могу найти необходимые вам товары. Но сначал введите пожалуйста размер вашей одежды.
    

    state: waitForClothingSize
        intent!: /РазмерОдежды
        script:
            $session.clothingSize = $request.query.match(/\d+/)[0]; // Сохраняем только числовое обозначение размера одежды
        a: теперь ваш размер одежды равен "{{$session.clothingSize}}"   
    
    
    
    state: choiseCasual
        intent!: /Повседневный
        script:
            $session.userstyle = 'обычная';
        a: Я запомню что вы предпочитаете обычную одежду
    state: choiseClassic
        intent!: /Классическая
        script:
            $session.userstyle = 'классическая';
        a: Я запомню что вы предпочитаете классическую одежду
    state: choiseSport
        intent!: /Спортивная
        script:
            $session.userstyle = 'спортивная';
        a: Я запомню что вы предпочитаете спортивную одежду
    
    
    
    
    
    state: Cold
        intent!: /Холодно
        script:
            $session.feeling = 'холодно'; // Сохраняем введенный сезон
        a: Подскажите, какой сейчас сезон года.
        event: waitForSeason // Указываем событие, которое будет обрабатывать следующий ввод
    
    state: Norm
        intent!: /Тепло
        script:
            $session.feeling = 'тепло'; // Сохраняем введенный сезон
        a: Подскажите, какой сейчас сезон года.
        event: waitForSeason // Указываем событие, которое будет обрабатывать следующий ввод
    
    state: Warm
        intent!: /Жарко
        script:
            $session.feeling = 'жарко'; // Сохраняем введенный сезон
        a: Подскажите, какой сейчас сезон года.
        event: waitForSeason // Указываем событие, которое будет обрабатывать следующий ввод
    
    state: waitForSeason
        intent!: /Сезон
        script:
            $session.season = $request.query; // Сохраняем введенный сезон
            $session.lastrecomendarray  = fetchRecomendation($session.season, $session.feeling, $session.userstyle);
            serchprodbyrecomendations($session.lastrecomendarray, $session.clothingSize);
    
    state: recomend
        intent!: /Рекомендации
        a: Давайте посмотрим что я могу вам посоветовать.
        script:
            $session.lastrecomendarray  = fetchbystile($session.userstyle);
            serchprodbyrecomendations($session.lastrecomendarray, $session.clothingSize);
        



    
    state: Search
        intent!: /Поиск
        script:
            // Извлекаем ключевую часть запроса
            $session.query = $request.query.replace(/(нужны|нужно|найди|поищи|нужна)/gi, "").trim();
            $session.query = $session.query.charAt(0).toUpperCase() + $session.query.slice(1); // Делаем первую букву заглавной
        a: Вы хотите найти "{{$session.query}}"?
        event: confirm
    
    state: Confirm
        intent!: /Согласие
        a: Вcе что нашел по запросу "{{$session.query}}"
        script:
            searchprod($session.query, $session.clothingSize);
    state: LastSearch
        intent!: /Последний
        a: Ваш последний запрос "{{$session.query}}"
        script: 
            searchprod($session.query);
    
    
    
    
     
    state: Bye
        intent!: /пока
        a: Пока-пока!

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}
