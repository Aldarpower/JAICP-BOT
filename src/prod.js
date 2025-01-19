function fetchProducts(query) {
    var out = $http.get("https://real-time-product-search.p.rapidapi.com/search?country=ru&q=" + encodeURIComponent(query) + "&language=ru&page=1&limit=1&sort_by=BEST_MATCH&product_condition=ANY&min_rating=ANY", {
        headers: {
            'x-rapidapi-key': 'a836a3e9a6mshd73490b59ae3d20p1eac94jsn136c5e3ada64', // Замените на ваш ключ
            'x-rapidapi-host': 'real-time-product-search.p.rapidapi.com'
        },
        timeout: 10000
    });
    return out;
}

// Функция для вывода товаров
function showProductPage(page, products) {
    var pageSize = 1;

    // Вычисляем индекс начала и конца текущей страницы
    var startIndex = page * pageSize;
    var endIndex = Math.min(startIndex + pageSize, products.length);

    var message = "";

    // Формируем сообщение с товарами для текущей страницы
    products.slice(startIndex, endIndex).forEach(function (product) {
        message += "- **" + product.product_title + "**\n" +
                   "  Рейтинг: " + product.product_rating + "\n" +
                   "  Цена: " + product.offer.price + "\n" +
                   "  [Купить здесь](" + product.offer.offer_page_url + ")\n\n";
    });

    $reactions.answer(message);
    
}

function searchprod(query, size){
    var g = query + " " + size + " размера";
            fetchProducts(g).then(function(response) {
                if (response.status === "OK") {
                    var products = response.data.products;
                    showProductPage(0, products); // Показываем первую страницу
                    
                } else {
                    $reactions.answer("Ошибка при получении данных: " + response.data.status);
                }
            }).catch(function(error) {
                $reactions.answer("Ошибка сети: " + error.message);
            });
}

function fetchRecomendation(season, feeling, style){
    var out = $http.get("aldarpower.pythonanywhere.com/api/clothing/all?season="+encodeURIComponent(season)+"&feeling="+encodeURIComponent(feeling)+"&style="+encodeURIComponent(style));
    var result = out.data.recommendations;
    var message = result.join(", ");
    var newmessage = "Рекомендуем купить: \n" + message;
    $reactions.answer(newmessage);
    return result;
}

function serchprodbyrecomendations(arr, size)
{
    arr.forEach(function(recommendation) {
        searchprod(recommendation, size);
    });    
}