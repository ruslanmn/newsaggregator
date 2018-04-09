package application;


public class SearchController {
    /*@FXML
    private TextField searchText;

    @FXML
    private ListView searchResultsListView;

    @FXML
    public void initialize() {
        NewsAggregatorApp.app.searchController = this;
    }

    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        String query = searchText.getText();
        List<SearchResultItem> searchResults = NewsAggregatorApp.app.documentProcessor.search(query, 0);

        List<String> values = searchResults.parallelStream().map( searchResultItem -> {
            String title = searchResultItem.getNewsDocument().getTitle();
            String keywords = searchResultItem.getKeywordsString(" ");
            return title + "\n" + keywords;
        }).collect(Collectors.toList());

        for(SearchResultItem searchResultItem : searchResults) {
            NewsDocument newsDocument = searchResultItem.getNewsDocument();
            System.out.println(newsDocument.getTitle());
            System.out.println(newsDocument.getContent());
            System.out.println("------------------------------------------------------------------");
        }

        searchResultsListView.getItems().setAll(values);
    }
*/
}
