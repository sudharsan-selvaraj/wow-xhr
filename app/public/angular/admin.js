var myApp = angular.module('myApp', ['ng-admin']);

myApp.config(['NgAdminConfigurationProvider', function (nga) {
    var admin = nga.application('My First Admin')
        .baseApiUrl(location.origin+'/api/delay/'); // main API endpoint

    var comment = nga.entity('comments'); // the API endpoint for users will be 'http://jsonplaceholder.typicode.com/comments/:id
    admin.addEntity(comment);

    var user = nga.entity('users'); // the API endpoint for users will be 'http://jsonplaceholder.typicode.com/users/:id
    user.listView()
        .fields([
            nga.field('name').isDetailLink(true),
            nga.field('username'),
            nga.field('email')
        ]);
    user.creationView().fields([
        nga.field('name')
            .validation({ required: true, minlength: 3, maxlength: 100 }),
        nga.field('username')
            .attributes({ placeholder: 'No space allowed, 5 chars min' })
            .validation({ required: true, pattern: '[A-Za-z0-9\.\-_]{5,20}' }),
        nga.field('email', 'email')
            .validation({ required: true }),
        nga.field('address.street')
            .label('Street'),
        nga.field('address.city')
            .label('City'),
        nga.field('address.zipcode')
            .label('Zipcode')
            .validation({ pattern: '[A-Z\-0-9]{5,10}' }),
        nga.field('phone'),
        nga.field('website')
            .validation({ validator: function(value) {
                    if (value.indexOf('http://') !== 0) throw new Error ('Invalid url in website');
                } })
    ]);
    user.editionView().fields(user.creationView().fields());
    admin.addEntity(user)

    var post = nga.entity('posts'); // the API endpoint for users will be 'http://jsonplaceholder.typicode.com/users/:id
    post.readOnly();
    post.listView()
        .fields([
            nga.field('title').isDetailLink(true),
            nga.field('body', 'text')
                .map(function truncate(value) {
                    if (!value) return '';
                    return value.length > 50 ? value.substr(0, 50) + '...' : value;
                }),
            nga.field('userId', 'reference')
                .targetEntity(user)
                .targetField(nga.field('username'))
                .label('Author')
        ])
        .listActions(['show'])
        .batchActions([])
        .filters([
            nga.field('q')
                .label('')
                .pinned(true)
                .template('<div class="input-group"><input type="text" ng-model="value" placeholder="Search" class="form-control"></input><span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span></div>'),
            nga.field('userId', 'reference')
                .targetEntity(user)
                .targetField(nga.field('username'))
                .label('User')
        ]);
    post.showView().fields([
        nga.field('title'),
        nga.field('body', 'text'),
        nga.field('userId', 'reference')
            .targetEntity(user)
            .targetField(nga.field('username'))
            .label('User'),
        nga.field('comments', 'referenced_list')
            .targetEntity(nga.entity('comments'))
            .targetReferenceField('postId')
            .targetFields([
                nga.field('email'),
                nga.field('name')
            ])
            .sortField('id')
            .sortDir('DESC'),
    ]);
    admin.addEntity(post);

    admin.menu(nga.menu()
        .addChild(nga.menu(user).icon('<span class="glyphicon glyphicon-user"></span>'))
        .addChild(nga.menu(post).icon('<span class="glyphicon glyphicon-pencil"></span>'))
    );

    nga.configure(admin);
}]);

myApp.config(['RestangularProvider', function (RestangularProvider) {
    RestangularProvider.addFullRequestInterceptor(function(element, operation, what, url, headers, params) {
        console.log("API called "+ url)
        if (operation == "getList") {
            // custom pagination params
            if (params._page) {
                params._start = (params._page - 1) * params._perPage;
                params._end = params._page * params._perPage;
            }
            delete params._page;
            delete params._perPage;
            // custom sort params
            if (params._sortField) {
                params._sort = params._sortField;
                params._order = params._sortDir;
                delete params._sortField;
                delete params._sortDir;
            }
            // custom filters
            if (params._filters) {
                for (var filter in params._filters) {
                    params[filter] = params._filters[filter];
                }
                delete params._filters;
            }
        }
        return { params: params };
    });
}]);