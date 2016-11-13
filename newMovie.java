//NewMovies are more popular and so cost more baseline and have more severe late penalties.
class NewMovie extends Movie
{
  NewMovie(String newTitle)
  {
    super(newTitle);
    cost = 5.00;
  }

  Public int rentalCost(int days)
  {
    if(days <= 2)
    {
      return cost * days;
    }
    else
    {
      return (cost * 3) + (cost * 2 * (days - 2));
    }
  }

  Public String getType()
  {
    return "New";
  }
}
