// (function($, ns, channel, window) {
//     var displayAuthTagsInst;
//     var coralPopOver = document.createElement('coral-popover');
//     var coralHeader = document.createElement('coral-popover-header');
//     var coralPopOverContent = document.createElement('coral-popover-content');

//     coralPopOver.append(coralPopOverContent);
//     coralPopOver.append(coralHeader);
//     coralPopOver.setAttribute('id', 'demoButton');
//     coralPopOver.setAttribute('placement', 'bottom');
            
//     var DisplayAuthTags = ns.util.createClass({
//         constructor: function CustomCarouselReorder(config) {
//             var that = this;
//             this._config = config;
//             that._init();
//         },

//         _init: function() {
//             this._displayAuthTagsInDropdown();
//         },

//         _displayAuthTagsInDropdown: function() {
//             console.log(this._config);
//             var componentProperties = Granite.HTTP.eval(this._config.editable.path + '.infinity.json');
//             console.log(componentProperties);
//             var authTags = componentProperties['cq:authtags'];
//             var denyTags = componentProperties['denyTags'];

//             coralHeader.innerText = 'Auth Tags';
//             authTags.forEach(function(index, authTag) {
//                 var p = document.createElement('p');
//                 p.setAttribute('class', 'u-coral-margin');
//                 p.innerText = index;
//                 coralPopOverContent.append(p);
//             });
//             displayAuthTagsInst = null;
//         },
//     });

//     // adds item to toolbar on condition
//     var displayAuthTags = new ns.ui.ToolbarAction({
//         name: 'DISPLAY_AUTH_TAGS',
//         text: Granite.I18n.get('Display Auth Tags'),
//         icon: 'coral-Icon--tags',
//         order: 'before PANEL_SELECT',
//         render: function ($el) {
//             $('#ContentScrollView').append(coralPopOver);
//             if (coralPopOver) {
//                 coralPopOver.target = $el[0];
//             }

//             return $el;
//         },
//         execute: function(editable, param, target) {
//             if (!displayAuthTagsInst) {
//                 new DisplayAuthTags({
//                     editable: editable,
//                 });
//             }
            
//             return false;
//         },
//         condition: function(editable) {
//             return true;
//         },
//         isNonMulti: true,
//     });

//     // when a component is getting ready to be edited
//     channel.on('cq-layer-activated', function(event) {
//         if (event.layer === 'Edit') {
//             // register CUSTOM_CAROUSEL_REORDER as an icon to be added.
//             ns.EditorFrame.editableToolbar.registerAction('DISPLAY_AUTH_TAGS', displayAuthTags);
//         }
//     });
// }
// )(jQuery, Granite.author, jQuery(document), this);
