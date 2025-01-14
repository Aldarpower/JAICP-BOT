require: prod.js
require: slotfilling/slotFilling.sc
  module = sys.zb-common

theme: /

    state: Start
        q!: $regex</start>
        a: Приветствую, я могу найти необходимые вам товары.

    state: Search
        intent!: /Поиск
        script:
            // Извлекаем ключевую часть запроса
            $session.query = $request.query.replace(/(нужны|)/gi, "").trim();
            $session.query = $session.query.charAt(0).toUpperCase() + $session.query.slice(1); // Делаем первую букву заглавной
        a: Вы хотите найти "{{$session.query}}"?
        event: confirm

    state: Confirm
        intent!: /Согласие
        a: Вcе что нашел по запросу "{{$session.query}}"
        script:
            searchprod($session.query);
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