(ns status-im.discovery.screen
  (:require-macros [status-im.utils.views :refer [defview]])
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [clojure.string :as str]
    [status-im.components.react :refer [view
                                        scroll-view
                                        text
                                        text-input]]
    [status-im.components.toolbar.view :refer [toolbar]]
    [status-im.components.drawer.view :refer [open-drawer]]
    [status-im.discovery.views.popular :refer [discovery-popular]]
    [status-im.discovery.views.recent :refer [discovery-recent]]
    [status-im.discovery.styles :as st]
    [status-im.components.tabs.bottom-gradient :refer [bottom-gradient]]
    [status-im.i18n :refer [label]]))

(defn get-hashtags [status]
  (let [hashtags (map #(str/lower-case (str/replace % #"#" "")) (re-seq #"[^ !?,;:.]+" status))]
    (or hashtags [])))

(defn title-content [show-search?]
  [view st/discovery-toolbar-content
   (if show-search?
     [text-input {:style           st/discovery-search-input
                  :autoFocus       true
                  :placeholder     (label :t/search-tags)
                  :onSubmitEditing (fn [e]
                                     (let [search   (aget e "nativeEvent" "text")
                                           hashtags (get-hashtags search)]
                                       (dispatch [:set :discovery-search-tags hashtags])
                                       (dispatch [:navigate-to :discovery-search-results])))}]
     [view
      [text {:style st/discovery-title
             :font  :default}
       (label :t/discovery)]])])

(defn toogle-search [current-value]
  (dispatch [:set ::show-search? (not current-value)]))

(defn discovery-toolbar [show-search?]
  [toolbar
   {:style          st/discovery-toolbar
    :nav-action     {:image   {:source {:uri :icon_hamburger}
                               :style  st/hamburger-icon}
                     :handler open-drawer}
    :custom-content [title-content show-search?]
    :actions        [{:image   {:source {:uri :icon_search}
                                :style  st/search-icon}
                      :handler #(toogle-search show-search?)}]}])

(defview discovery []
  [show-search? [:get ::show-search?]
   contacts [:get :contacts]]
  [view st/discovery-container
   [discovery-toolbar show-search?]
   [scroll-view st/scroll-view-container

    [view st/section-spacing
     [text {:style st/discovery-subtitle
            :font  :medium}
      (label :t/popular-tags)]]
    [discovery-popular {:contacts contacts}]

    [view st/section-spacing
     [text {:style st/discovery-subtitle
            :font  :medium}
      (label :t/recent)]]
    [discovery-recent {:contacts contacts}]]

   [bottom-gradient]])
