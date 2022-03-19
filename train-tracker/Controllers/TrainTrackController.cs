using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace train_tracker.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class TrainTrackController : ControllerBase
    {
        private readonly ILogger<TrainTrackController> _logger;

        public TrainTrackController(ILogger<TrainTrackController> logger)
        {
            _logger = logger;
        }

        [HttpPost]
        public IEnumerable<int> Post([FromBody]TrainQuery query)
        {
            string origin = query.Origin;
            string destination = query.Destination;

            return new List<int>() { 50, 60 };
        }
    }
}
