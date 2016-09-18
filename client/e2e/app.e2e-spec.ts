import { HelfomatClientPage } from './app.po';

describe('helfomat-client App', function() {
  let page: HelfomatClientPage;

  beforeEach(() => {
    page = new HelfomatClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
