$(document).ready(function () {
            var rss = (function ($) {
                var createWidgets = function () {
                    $('#mainSplitter').jqxSplitter({  width: '100%', height: '100%', panels: [{ size: 200, min: 100 }, {min: 200, size: '100%'}] });
                    $('#contentSplitter').jqxSplitter({ width: '100%', height: '100%',  orientation: 'horizontal', panels: [{ size: 400, min: 100, collapsible: false }, { min: 100, collapsible: true}] });
                    $("#feedExpander").jqxExpander({toggleMode: 'none', showArrow: false, width: "100%", height: "100%", 
                        initContent: function () {
                            $('#jqxTree').jqxTree({ height: '100%', width: '100%'});
                        }
                    });
                    $("#feedContentExpander").jqxExpander({ toggleMode: 'none', showArrow: false, width: "100%", height: "100%",  initContent: function () {
                         $('#feedItemContent').jqxPanel({  width: '100%', height: '100%' });
                    }
                    });
                    $("#feedListExpander").jqxExpander({
                        toggleMode: 'none', showArrow: false, width: "100%", height: "100%",  initContent: function () {
                        $('#feedListContainer').jqxListBox({  source: ['1'], width: '100%', height: '100%' });
                    }
                    });
                };
                var addEventListeners = function () {
                    $('#jqxTree').on('select', function (event) {
                        var item = $('#jqxTree').jqxTree('getItem', event.args.element);
                        getFeed(config.feeds[item.label]);
                    });
                    $('#feedListContainer').on('select', function (event) {
                        loadContent(event.args.index);
                    });
                };
                var getFeed = function (feed) {
                    config.currentFeed = feed;
                    feed = config.dataDir + '/' + feed + '.' + config.format;
                    loadFeed(feed);
                };
                var loadFeed = function (feed, callback) {
                    $.ajax({
                        'dataType': 'json',
                        'url': feed,
                        success: function (data) {
                            var channel = data.rss.channel;
                            config.currentFeedContent = channel.item;
                            displayFeedList(config.currentFeedContent);
                            displayFeedHeader(channel.title);
                            selectFirst();
                        }
                    });
                };
                var selectFirst = function () {
                    $('#feedListContainer').jqxListBox('selectIndex', 0);
                    loadContent(0);
                };
                var displayFeedHeader = function (header) {
                    $("#feedListExpander").jqxExpander('setHeaderContent', header);
                };
                var displayFeedList = function (items) {
                    var headers = getHeaders(items);
                    config.feedListContainer.jqxListBox('source', headers);
                };
                var getHeaders = function (items) {
                    var headers = [], header;
                    for (var i = 0; i < items.length; i += 1) {
                        header = items[i].title;
                        headers.push(header);
                    }
                    return headers;
                };
                var loadContent = function (index) {
                    var item = config.currentFeedContent[index];
                    if (item != null) {
                        config.feedItemContent.jqxPanel('clearcontent');
                        config.feedItemContent.jqxPanel('prepend', '<div style="padding: 1px;"><span>' + item.description + '</span></div>');
                        addContentHeaderData(item);
                        config.selectedIndex = index;
                    }
                };
                var addContentHeaderData = function (item) {
                    var link = $('<a style="white-space: nowrap; margin-left: 15px;" target="_blank">Source</a>'),
                        date = $('<div style="white-space: nowrap; margin-left: 30px;">' + item.pubDate + '</div>');
                    container = $('<table height="100%"><tr><td></td><td></td></tr></table>');
                    link[0].href = item.link;
                    config.feedItemHeader.empty();
                    config.feedItemHeader.append(container);
                    container.find('td:first').append(link);
                    container.find('td:last').append(date);
                    $("#feedContentExpander").jqxExpander('setHeaderContent', container[0].outerHTML);
                };
                var dataDir = 'data';
                var config = {
                    feeds: { 'CNN.com': 'cnn', 'Geek.com': 'geek', 'ScienceDaily': 'sciencedaily' },
                    format: 'txt',
                    dataDir: dataDir,
                    feedTree: $('#jqxTree'),
                    feedListContainer: $('#feedListContainer'),
                    feedItemContent: $('#feedItemContent'),
                    feedItemHeader: $('#feedItemHeader'),
                    feedUpperPanel: $('#feedUpperPanel'),
                    feedHeader: $('#feedHeader'),
                    feedContentArea: $('#feedContentArea'),
                    selectedIndex: -1,
                    currentFeed: '',
                    currentFeedContent: {}
                };
                return {
                    init: function () {
                        createWidgets();
                        addEventListeners();
                        getFeed('sciencedaily');
                    }
                }
            } (jQuery));
            rss.init();
        });